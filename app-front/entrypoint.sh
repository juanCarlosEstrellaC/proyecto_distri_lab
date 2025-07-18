#!/bin/sh

# Generar archivo de configuración con variables de entorno
cat > /usr/share/nginx/html/config.js << EOF
window.APP_CONFIG = {
  API_BASE_URL: '${VITE_API_BASE_URL:-http://localhost:8080}',
  AUTHORS_API_URL: '${VITE_AUTHORS_API_URL:-http://localhost:8080/app-authors}',
  BOOKS_API_URL: '${VITE_BOOKS_API_URL:-http://localhost:8080/app-books}',
  CUSTOMERS_API_URL: '${VITE_CUSTOMERS_API_URL:-http://localhost:8080/app-customers}'
};
EOF

# Iniciar nginx
nginx -g "daemon off;"
