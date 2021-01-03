export default Object.freeze({
  ENV: process.env.NODE_ENV || "development",
  API_HOST: process.env.VUE_APP_BASE_API_URL || "http://localhost:8080/",
  // Locale
  LOCALE: process.env.VUE_APP_LOCALE || "ko-KR",
  DATE_FORMAT_STRING: process.env.VUE_APP_DATE_FORMAT_STRING || "YYYY-MM-DD",
  DATETIME_FORMAT_STRING:
      process.env.VUE_APP_DATETIME_FORMAT_STRING || "YYYY-MM-DD HH:mm:ss",
  TIME_FORMAT_STRING: process.env.VUE_APP_TIME_FORMAT_STRING || "HH:mm:ss",
  DATETIME_MINUTE_FORMAT_STRING:
      process.env.VUE_APP_DATETIME_MINUTE_FORMAT_STRING || "YYYY-MM-DD HH:mm",
  TIME_MINUTE_FORMAT_STRING:
      process.env.VUE_APP_TIME_MINUTE_FORMAT_STRING || "HH:mm",
  // DataTables
  FOOTER_PROPS_MAX_1000: {
    showFirstLastPage: true,
    itemsPerPageOptions: [20, 50, 100, 300, 500, 1000],
  },
});
