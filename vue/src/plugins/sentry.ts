import Vue from 'vue';
import * as Sentry from '@sentry/browser';
import * as Integrations from '@sentry/integrations';

if (process.env.SENTRY_DSN) {
  Sentry.init({
    dsn: process.env.SENTRY_DSN,
    // @ts-ignore
    integrations: [new Integrations.Vue({ Vue, attachProps: true })],
  });
}
