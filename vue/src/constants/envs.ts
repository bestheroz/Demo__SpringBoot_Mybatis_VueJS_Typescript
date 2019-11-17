export default Object.freeze({
  // 기본값은 .env.local 로 맞추도록 하자.
  APP_NAME:
    'My Spring + Boot + Mybatis + SwaggerUI + Typescript + VueJs + Vuetify',
  APP_VERSIONS: require('../../package.json').version,
  ENV: process.env.NODE_ENV || 'local',
  HOST: process.env.VUE_APP_BASE_API_URL || 'http://localhost:8080/',
  LANGUAGE: process.env.VUE_APP_LANGUAGE || 'ko',
  DATE_FORMAT_STRING: process.env.DATE_FORMAT_STRING || 'YYYY-MM-DD',
  DATE_TIME_FORMAT_STRING:
    process.env.DATE_TIME_FORMAT_STRING || 'YYYY-MM-DD HH:mm:ss',
  TIME_FORMAT_STRING: process.env.TIME_FORMAT_STRING || 'HH:mm',
});
