# Script para probar los métodos REST de app-books
# Configuración base - A través de Traefik (puerto 8080 del host mapea al puerto 80 de Traefik)
$baseUrl = "http://localhost:8080/app-books"
$headers = @{"Content-Type" = "application/json"}

Write-Host "=== PRUEBAS DE LA API DE LIBROS ===" -ForegroundColor Green
Write-Host "URL base: $baseUrl (a través de Traefik)" -ForegroundColor Yellow
Write-Host ""

# Función para hacer peticiones HTTP con manejo de errores
function Invoke-RestMethodSafe {
    param(
        [string]$Uri,
        [string]$Method = "GET",
        [object]$Body = $null,
        [hashtable]$Headers = @{}
    )
    
    try {
        $params = @{
            Uri = $Uri
            Method = $Method
            Headers = $Headers
        }
        
        if ($Body) {
            $params.Body = $Body
        }
        
        $response = Invoke-RestMethod @params
        return @{
            Success = $true
            Data = $response
            StatusCode = 200
        }
    }
    catch {
        $statusCode = $_.Exception.Response.StatusCode.value__
        $statusDescription = $_.Exception.Response.StatusDescription
        return @{
            Success = $false
            Error = $_.Exception.Message
            StatusCode = $statusCode
            StatusDescription = $statusDescription
        }
    }
}

# Función para mostrar resultados
function Show-Result {
    param(
        [string]$TestName,
        [object]$Result
    )
    
    Write-Host "📋 $TestName" -ForegroundColor Cyan
    if ($Result.Success) {
        Write-Host "✅ ÉXITO - Status: $($Result.StatusCode)" -ForegroundColor Green
        if ($Result.Data) {
            Write-Host "📄 Respuesta:" -ForegroundColor White
            $Result.Data | ConvertTo-Json -Depth 3 | Write-Host
        }
    } else {
        Write-Host "❌ ERROR - Status: $($Result.StatusCode) - $($Result.StatusDescription)" -ForegroundColor Red
        Write-Host "🔍 Detalle: $($Result.Error)" -ForegroundColor Yellow
    }
    Write-Host "-" * 80
}

# 1. Probar GET /books (obtener todos los libros)
Write-Host "1. Probando GET /books (obtener todos los libros)" -ForegroundColor Magenta
$result = Invoke-RestMethodSafe -Uri "$baseUrl/books" -Method "GET" -Headers $headers
Show-Result "GET /books" $result

# 2. Probar POST /books (crear un nuevo libro)
Write-Host "2. Probando POST /books (crear un nuevo libro)" -ForegroundColor Magenta
$newBook = @{
    isbn = "978-0-123456-78-9"
    title = "Cien años de soledad"
    price = 29.99
    version = 1
} | ConvertTo-Json

$result = Invoke-RestMethodSafe -Uri "$baseUrl/books" -Method "POST" -Body $newBook -Headers $headers
Show-Result "POST /books" $result

# Guardar el ISBN del libro creado para las siguientes pruebas
$bookIsbn = $null
if ($result.Success -and $result.Data.isbn) {
    $bookIsbn = $result.Data.isbn
    Write-Host "💾 ISBN del libro creado: $bookIsbn" -ForegroundColor Yellow
}

# 3. Probar GET /books/{isbn} (obtener un libro específico)
if ($bookIsbn) {
    Write-Host "3. Probando GET /books/$bookIsbn (obtener libro específico)" -ForegroundColor Magenta
    $result = Invoke-RestMethodSafe -Uri "$baseUrl/books/$bookIsbn" -Method "GET" -Headers $headers
    Show-Result "GET /books/$bookIsbn" $result
} else {
    Write-Host "3. ⚠️  Saltando GET /books/{isbn} - No se pudo crear el libro" -ForegroundColor Yellow
}

# 4. Probar PUT /books/{isbn} (actualizar un libro)
if ($bookIsbn) {
    Write-Host "4. Probando PUT /books/$bookIsbn (actualizar libro)" -ForegroundColor Magenta
    $updatedBook = @{
        isbn = $bookIsbn
        title = "Cien años de soledad (Edición actualizada)"
        price = 34.99
        version = 2
    } | ConvertTo-Json

    $result = Invoke-RestMethodSafe -Uri "$baseUrl/books/$bookIsbn" -Method "PUT" -Body $updatedBook -Headers $headers
    Show-Result "PUT /books/$bookIsbn" $result
} else {
    Write-Host "4. ⚠️  Saltando PUT /books/{isbn} - No se pudo crear el libro" -ForegroundColor Yellow
}

# 5. Probar casos de error
Write-Host "5. Probando casos de error" -ForegroundColor Magenta

# 5.1 GET libro inexistente
$result = Invoke-RestMethodSafe -Uri "$baseUrl/books/978-0-999999-99-9" -Method "GET" -Headers $headers
Show-Result "GET /books/978-0-999999-99-9 (libro inexistente)" $result

# 5.2 POST libro sin título
$invalidBook = @{
    isbn = "978-0-123456-79-0"
    title = ""
    price = 25.99
    version = 1
} | ConvertTo-Json

$result = Invoke-RestMethodSafe -Uri "$baseUrl/books" -Method "POST" -Body $invalidBook -Headers $headers
Show-Result "POST /books (sin título)" $result

# 5.3 POST libro sin ISBN
$invalidBook2 = @{
    title = "Libro sin ISBN"
    price = 25.99
    version = 1
} | ConvertTo-Json

$result = Invoke-RestMethodSafe -Uri "$baseUrl/books" -Method "POST" -Body $invalidBook2 -Headers $headers
Show-Result "POST /books (sin ISBN)" $result

# 5.4 POST libro con ISBN duplicado (si ya existe)
if ($bookIsbn) {
    Write-Host "5.4 Probando POST con ISBN duplicado" -ForegroundColor Magenta
    $duplicateBook = @{
        isbn = $bookIsbn
        title = "Otro libro con el mismo ISBN"
        price = 19.99
        version = 1
    } | ConvertTo-Json

    $result = Invoke-RestMethodSafe -Uri "$baseUrl/books" -Method "POST" -Body $duplicateBook -Headers $headers
    Show-Result "POST /books (ISBN duplicado)" $result
}

# 6. Probar DELETE /books/{isbn} (eliminar libro) - Al final para no afectar otras pruebas
if ($bookIsbn) {
    Write-Host "6. Probando DELETE /books/$bookIsbn (eliminar libro)" -ForegroundColor Magenta
    $result = Invoke-RestMethodSafe -Uri "$baseUrl/books/$bookIsbn" -Method "DELETE" -Headers $headers
    Show-Result "DELETE /books/$bookIsbn" $result
} else {
    Write-Host "6. ⚠️  Saltando DELETE /books/{isbn} - No se pudo crear el libro" -ForegroundColor Yellow
}

# 7. Verificar que el libro fue eliminado
if ($bookIsbn) {
    Write-Host "7. Verificando que el libro fue eliminado" -ForegroundColor Magenta
    $result = Invoke-RestMethodSafe -Uri "$baseUrl/books/$bookIsbn" -Method "GET" -Headers $headers
    Show-Result "GET /books/$bookIsbn (después de eliminar)" $result
}

Write-Host ""
Write-Host "=== RESUMEN DE PRUEBAS COMPLETADAS ===" -ForegroundColor Green
Write-Host "✅ Se han probado todos los endpoints principales de la API de libros" -ForegroundColor Green
Write-Host "📋 Métodos probados:" -ForegroundColor White
Write-Host "   - GET /books (listar todos)" -ForegroundColor Gray
Write-Host "   - POST /books (crear)" -ForegroundColor Gray
Write-Host "   - GET /books/{isbn} (obtener específico)" -ForegroundColor Gray
Write-Host "   - PUT /books/{isbn} (actualizar)" -ForegroundColor Gray
Write-Host "   - DELETE /books/{isbn} (eliminar)" -ForegroundColor Gray
Write-Host "   - Casos de error (404, 400, 409)" -ForegroundColor Gray
