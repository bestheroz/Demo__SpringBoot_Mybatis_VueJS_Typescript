import * as Sentry from "@sentry/browser";
import { Vue as VueIntegration } from "@sentry/integrations";
import { Integrations } from "@sentry/tracing";
import Vue from "vue";

if (process.env.VUE_APP_SENTRY_DSN) {
  Sentry.init({
    dsn: process.env.VUE_APP_SENTRY_DSN,
    integrations: [
      new Integrations.BrowserTracing(),
      new VueIntegration({
        Vue,
        tracing: true,
      }),
    ],
    tracesSampleRate: 1.0, // Be sure to lower this in production
  });
}
