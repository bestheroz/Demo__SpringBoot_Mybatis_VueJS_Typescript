export default Object.freeze({
  // 로컬 개발 값은 .env.local (파일)에 맞추도록 하자.
  APP_VERSIONS: require('../../package.json').version,
  ENV: process.env.NODE_ENV || 'local',
  API_HOST: process.env.VUE_APP_BASE_URL || 'http://localhost:8080/',
  // Locale
  LOCALE: process.env.VUE_APP_LOCALE || 'ko-KR',
  DATE_FORMAT_STRING: process.env.VUE_APP_DATE_FORMAT_STRING || 'YYYY-MM-DD',
  DATETIME_FORMAT_STRING:
    process.env.VUE_APP_DATETIME_FORMAT_STRING || 'YYYY-MM-DD HH:mm:ss',
  TIME_FORMAT_STRING: process.env.VUE_APP_TIME_FORMAT_STRING || 'HH:mm:ss',
  DATETIME_MINUTE_FORMAT_STRING:
    process.env.VUE_APP_DATETIME_MINUTE_FORMAT_STRING || 'YYYY-MM-DD HH:mm',
  TIME_MINUTE_FORMAT_STRING:
    process.env.VUE_APP_TIME_MINUTE_FORMAT_STRING || 'HH:mm',
  // DataTables
  FOOTER_PROPS_100: {
    showFirstLastPage: true,
    firstIcon: 'mdi-page-first',
    lastIcon: 'mdi-page-last',
    itemsPerPageOptions: [100, 300, 500, 1000, 3000, 5000, 10000],
  },
});
