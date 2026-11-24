export function getErrorMessage(error) {
  return error.response?.data?.message
    || (error.code === 'ECONNABORTED' ? 'La API tardó demasiado' : null)
    || (error.response ? `Error HTTP ${error.response.status}` : null)
    || 'No se pudo contactar a la API. Revisa conexión y CORS.';
}
