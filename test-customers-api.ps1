# Script para probar los métodos REST de app-customers
# Configuración base - A través de Traefik (puerto 8080 del host mapea al puerto 80 de Traefik)
$baseUrl = "http://localhost:8080/app-customers"
$headers = @{"Content-Type" = "application/json"}

Write-Host "=== PRUEBAS DE LA API DE CUSTOMERS ===" -ForegroundColor Green
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
            $Result.Data | ConvertTo-Json -Depth 4 | Write-Host
        }
    } else {
        Write-Host "❌ ERROR - Status: $($Result.StatusCode) - $($Result.StatusDescription)" -ForegroundColor Red
        Write-Host "🔍 Detalle: $($Result.Error)" -ForegroundColor Yellow
    }
    Write-Host "-" * 80
}

# 1. Probar GET /ping (health check básico)
Write-Host "1. Probando GET /ping (health check básico)" -ForegroundColor Magenta
$result = Invoke-RestMethodSafe -Uri "$baseUrl/ping" -Method "GET" -Headers $headers
Show-Result "GET /ping" $result

# 2. Probar GET /orders/customer/{customerId} (obtener órdenes de un cliente)
Write-Host "2. Probando GET /orders/customer/1 (obtener órdenes del cliente 1)" -ForegroundColor Magenta
$result = Invoke-RestMethodSafe -Uri "$baseUrl/orders/customer/1" -Method "GET" -Headers $headers
Show-Result "GET /orders/customer/1" $result

# 3. Probar con otro cliente
Write-Host "3. Probando GET /orders/customer/2 (obtener órdenes del cliente 2)" -ForegroundColor Magenta
$result = Invoke-RestMethodSafe -Uri "$baseUrl/orders/customer/2" -Method "GET" -Headers $headers
Show-Result "GET /orders/customer/2" $result

# 4. Probar con un cliente que podría no existir
Write-Host "4. Probando GET /orders/customer/999 (cliente que podría no existir)" -ForegroundColor Magenta
$result = Invoke-RestMethodSafe -Uri "$baseUrl/orders/customer/999" -Method "GET" -Headers $headers
Show-Result "GET /orders/customer/999" $result

# 5. Probar GET /orders/{orderId} (obtener detalle de una orden específica)
Write-Host "5. Probando GET /orders/1 (obtener detalle de la orden 1)" -ForegroundColor Magenta
$result = Invoke-RestMethodSafe -Uri "$baseUrl/orders/1" -Method "GET" -Headers $headers
Show-Result "GET /orders/1" $result

# 6. Probar con otra orden
Write-Host "6. Probando GET /orders/2 (obtener detalle de la orden 2)" -ForegroundColor Magenta
$result = Invoke-RestMethodSafe -Uri "$baseUrl/orders/2" -Method "GET" -Headers $headers
Show-Result "GET /orders/2" $result

# 7. Probar con una orden que no existe
Write-Host "7. Probando GET /orders/999 (orden que no existe)" -ForegroundColor Magenta
$result = Invoke-RestMethodSafe -Uri "$baseUrl/orders/999" -Method "GET" -Headers $headers
Show-Result "GET /orders/999 (orden inexistente)" $result

# 8. Probar con diferentes IDs de clientes para ver la variedad de datos
Write-Host "8. Probando GET /orders/customer/3 (obtener órdenes del cliente 3)" -ForegroundColor Magenta
$result = Invoke-RestMethodSafe -Uri "$baseUrl/orders/customer/3" -Method "GET" -Headers $headers
Show-Result "GET /orders/customer/3" $result

# 9. Probar con un ID de cliente inválido (no numérico)
Write-Host "9. Probando GET /orders/customer/abc (ID de cliente inválido)" -ForegroundColor Magenta
$result = Invoke-RestMethodSafe -Uri "$baseUrl/orders/customer/abc" -Method "GET" -Headers $headers
Show-Result "GET /orders/customer/abc (ID inválido)" $result

# 10. Probar con un ID de orden inválido (no numérico)
Write-Host "10. Probando GET /orders/abc (ID de orden inválido)" -ForegroundColor Magenta
$result = Invoke-RestMethodSafe -Uri "$baseUrl/orders/abc" -Method "GET" -Headers $headers
Show-Result "GET /orders/abc (ID inválido)" $result

# 11. Probar el health check del servicio
Write-Host "11. Probando GET /q/health (health check del servicio)" -ForegroundColor Magenta
$result = Invoke-RestMethodSafe -Uri "$baseUrl/q/health" -Method "GET" -Headers $headers
Show-Result "GET /q/health" $result

# 12. Probar el health check de readiness
Write-Host "12. Probando GET /q/health/ready (readiness check)" -ForegroundColor Magenta
$result = Invoke-RestMethodSafe -Uri "$baseUrl/q/health/ready" -Method "GET" -Headers $headers
Show-Result "GET /q/health/ready" $result

# 13. Probar el health check de liveness
Write-Host "13. Probando GET /q/health/live (liveness check)" -ForegroundColor Magenta
$result = Invoke-RestMethodSafe -Uri "$baseUrl/q/health/live" -Method "GET" -Headers $headers
Show-Result "GET /q/health/live" $result

Write-Host ""
Write-Host "=== RESUMEN DE PRUEBAS COMPLETADAS ===" -ForegroundColor Green
Write-Host "✅ Se han probado todos los endpoints principales de la API de customers" -ForegroundColor Green
Write-Host "📋 Métodos probados:" -ForegroundColor White
Write-Host "   - GET /ping (health check básico)" -ForegroundColor Gray
Write-Host "   - GET /orders/customer/{customerId} (órdenes por cliente)" -ForegroundColor Gray
Write-Host "   - GET /orders/{orderId} (detalle de orden)" -ForegroundColor Gray
Write-Host "   - GET /q/health (health checks del servicio)" -ForegroundColor Gray
Write-Host "   - Casos de error (404, IDs inválidos)" -ForegroundColor Gray
Write-Host ""
Write-Host "🔍 Nota: Esta API es principalmente de consulta (solo GET)" -ForegroundColor Yellow
Write-Host "   - Muestra órdenes de compra con detalles de libros" -ForegroundColor Gray
Write-Host "   - Integra con el servicio de books para obtener información completa" -ForegroundColor Gray
