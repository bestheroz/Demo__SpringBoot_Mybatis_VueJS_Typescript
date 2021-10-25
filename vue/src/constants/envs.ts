import dotenvParseVariables from "dotenv-parse-variables";
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
const parsedEnv: { [key: string]: unknown } = dotenvParseVariables(process.env);

export default Object.freeze({
  PRODUCT_TITLE: parsedEnv.VUE_APP_PRODUCT_TITLE as string,
  PRODUCT_VERSION: parsedEnv.VUE_APP_PRODUCT_VERSION as string,
  API_HOST: parsedEnv.VUE_APP_BASE_API_URL as string,
  FILE_API_HOST: parsedEnv.VUE_APP_FILE_API_HOST as string,
  // Locale
  LOCALE: parsedEnv.VUE_APP_LOCALE as string,
  DATE_FORMAT_STRING: parsedEnv.VUE_APP_DATE_FORMAT_STRING as string,
  DATETIME_FORMAT_STRING: parsedEnv.VUE_APP_DATETIME_FORMAT_STRING as string,
  TIME_FORMAT_STRING: parsedEnv.VUE_APP_TIME_FORMAT_STRING as string,
  DATETIME_MINUTE_FORMAT_STRING:
    parsedEnv.VUE_APP_DATETIME_MINUTE_FORMAT_STRING as string,
  TIME_MINUTE_FORMAT_STRING:
    parsedEnv.VUE_APP_TIME_MINUTE_FORMAT_STRING as string,
  SENTRY_DSN: parsedEnv.VUE_APP_SENTRY_DSN as string,
  ENVIRONMENT: parsedEnv.VUE_APP_ENVIRONMENT as string,
  // DataTables
  FOOTER_PROPS_MAX_100: {
    showFirstLastPage: true,
    itemsPerPageOptions: [10, 20, 50, 100],
  },
});
