export default Object.freeze({
  // 로컬 개발 값은 .env.local (파일)에 맞추도록 하자.
  APP_VERSIONS: require('../../package.json').version,
  ENV: process.env.NODE_ENV || 'local',
  API_HOST: process.env.VUE_APP_BASE_URL || 'http://localhost:8080/',
  // Locale
  LOCALE: process.env.VUE_APP_LOCALE || 'ko-KR',
  TIMEZONE: process.env.VUE_APP_TIMEZONE || 'Asia/Seoul',
  TIMEZONE_OFFSET_STRING: process.env.VUE_APP_TIMEZONE_OFFSET_STRING || '+0900',
  // DataTables
  FOOTER_PROPS_100: {
    showFirstLastPage: true,
    firstIcon: 'mdi-page-first',
    lastIcon: 'mdi-page-last',
    itemsPerPageOptions: [100, 300, 500, 1000, 3000, 5000, 10000],
  },
});
