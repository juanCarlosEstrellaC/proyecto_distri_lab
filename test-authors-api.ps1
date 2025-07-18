# Script para probar los métodos REST de app-authors
# Configuración base - A través de Traefik (puerto 8080 del host mapea al puerto 80 de Traefik)
$baseUrl = "http://localhost:8080/app-authors"
$headers = @{"Content-Type" = "application/json"}

Write-Host "=== PRUEBAS DE LA API DE AUTORES ===" -ForegroundColor Green
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

# 1. Probar GET /authors (obtener todos los autores)
Write-Host "1. Probando GET /authors (obtener todos los autores)" -ForegroundColor Magenta
$result = Invoke-RestMethodSafe -Uri "$baseUrl/authors" -Method "GET" -Headers $headers
Show-Result "GET /authors" $result

# 2. Probar POST /authors (crear un nuevo autor)
Write-Host "2. Probando POST /authors (crear un nuevo autor)" -ForegroundColor Magenta
$newAuthor = @{
    name = "Gabriel García Márquez"
    version = 1
} | ConvertTo-Json

$result = Invoke-RestMethodSafe -Uri "$baseUrl/authors" -Method "POST" -Body $newAuthor -Headers $headers
Show-Result "POST /authors" $result

# Guardar el ID del autor creado para las siguientes pruebas
$authorId = $null
if ($result.Success -and $result.Data.id) {
    $authorId = $result.Data.id
    Write-Host "💾 ID del autor creado: $authorId" -ForegroundColor Yellow
}

# 3. Probar GET /authors/{id} (obtener un autor específico)
if ($authorId) {
    Write-Host "3. Probando GET /authors/$authorId (obtener autor específico)" -ForegroundColor Magenta
    $result = Invoke-RestMethodSafe -Uri "$baseUrl/authors/$authorId" -Method "GET" -Headers $headers
    Show-Result "GET /authors/$authorId" $result
} else {
    Write-Host "3. ⚠️  Saltando GET /authors/{id} - No se pudo crear el autor" -ForegroundColor Yellow
}

# 4. Probar PUT /authors/{id} (actualizar un autor)
if ($authorId) {
    Write-Host "4. Probando PUT /authors/$authorId (actualizar autor)" -ForegroundColor Magenta
    $updatedAuthor = @{
        name = "Gabriel García Márquez (actualizado)"
        version = 2
    } | ConvertTo-Json

    $result = Invoke-RestMethodSafe -Uri "$baseUrl/authors/$authorId" -Method "PUT" -Body $updatedAuthor -Headers $headers
    Show-Result "PUT /authors/$authorId" $result
} else {
    Write-Host "4. ⚠️  Saltando PUT /authors/{id} - No se pudo crear el autor" -ForegroundColor Yellow
}

# 5. Probar GET /authors/find/{isbn} (buscar autores por ISBN)
Write-Host "5. Probando GET /authors/find/978-0-123456-78-9 (buscar autores por ISBN)" -ForegroundColor Magenta
$result = Invoke-RestMethodSafe -Uri "$baseUrl/authors/find/978-0-123456-78-9" -Method "GET" -Headers $headers
Show-Result "GET /authors/find/978-0-123456-78-9" $result

# 6. Probar casos de error
Write-Host "6. Probando casos de error" -ForegroundColor Magenta

# 6.1 GET autor inexistente
$result = Invoke-RestMethodSafe -Uri "$baseUrl/authors/999999" -Method "GET" -Headers $headers
Show-Result "GET /authors/999999 (autor inexistente)" $result

# 6.2 POST autor sin nombre
$invalidAuthor = @{
    name = ""
    version = 1
} | ConvertTo-Json

$result = Invoke-RestMethodSafe -Uri "$baseUrl/authors" -Method "POST" -Body $invalidAuthor -Headers $headers
Show-Result "POST /authors (sin nombre)" $result

# 7. Probar DELETE /authors/{id} (eliminar autor) - Al final para no afectar otras pruebas
if ($authorId) {
    Write-Host "7. Probando DELETE /authors/$authorId (eliminar autor)" -ForegroundColor Magenta
    $result = Invoke-RestMethodSafe -Uri "$baseUrl/authors/$authorId" -Method "DELETE" -Headers $headers
    Show-Result "DELETE /authors/$authorId" $result
} else {
    Write-Host "7. ⚠️  Saltando DELETE /authors/{id} - No se pudo crear el autor" -ForegroundColor Yellow
}

# 8. Verificar que el autor fue eliminado
if ($authorId) {
    Write-Host "8. Verificando que el autor fue eliminado" -ForegroundColor Magenta
    $result = Invoke-RestMethodSafe -Uri "$baseUrl/authors/$authorId" -Method "GET" -Headers $headers
    Show-Result "GET /authors/$authorId (después de eliminar)" $result
}

Write-Host ""
Write-Host "=== RESUMEN DE PRUEBAS COMPLETADAS ===" -ForegroundColor Green
Write-Host "✅ Se han probado todos los endpoints principales de la API de autores" -ForegroundColor Green
Write-Host "📋 Métodos probados:" -ForegroundColor White
Write-Host "   - GET /authors (listar todos)" -ForegroundColor Gray
Write-Host "   - POST /authors (crear)" -ForegroundColor Gray
Write-Host "   - GET /authors/{id} (obtener específico)" -ForegroundColor Gray
Write-Host "   - PUT /authors/{id} (actualizar)" -ForegroundColor Gray
Write-Host "   - DELETE /authors/{id} (eliminar)" -ForegroundColor Gray
Write-Host "   - GET /authors/find/{isbn} (buscar por ISBN)" -ForegroundColor Gray
Write-Host "   - Casos de error (404, 400)" -ForegroundColor Gray
