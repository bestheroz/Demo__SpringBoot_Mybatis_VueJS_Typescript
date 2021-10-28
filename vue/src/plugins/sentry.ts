import * as Sentry from "@sentry/vue";
import { Integrations } from "@sentry/tracing";
import Vue from "vue";
import envs from "@/constants/envs";

if (envs.SENTRY_DSN && envs.ENVIRONMENT !== "local") {
  Sentry.init({
    Vue: Vue,
    dsn: envs.SENTRY_DSN,
    environment: envs.ENVIRONMENT,
    integrations: [new Integrations.BrowserTracing()],
    tracesSampleRate: envs.ENVIRONMENT === "production" ? 1.0 : 1.0, // Be sure to lower this in production
    tracingOptions: {
      trackComponents: true,
    },
  });
}
