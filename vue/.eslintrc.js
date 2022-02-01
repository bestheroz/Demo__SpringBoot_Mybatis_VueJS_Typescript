module.exports = {
  root: true,
  parser: "vue-eslint-parser",
  parserOptions: {
    sourceType: "module",
    ecmaVersion: 2021,
    parser: "@typescript-eslint/parser",
    ecmaFeatures: {
      globalReturn: false,
      impliedStrict: false,
      jsx: false,
    },
    vueFeatures: {
      filter: true,
      interpolationAsNonHTML: false,
      styleCSSVariableInjection: true,
    },
  },
  env: {
    node: true,
  },
  extends: [
    "plugin:vue/strongly-recommended",
    "eslint:recommended",
    "plugin:vuetify/base",
    "plugin:@typescript-eslint/recommended",
    "plugin:prettier/recommended",
  ],
  plugins: ["vue"],
  rules: {
    "no-console":
      process.env.NODE_ENV === "production"
        ? ["error", { allow: ["warn", "error"] }]
        : "off",
    "no-debugger": process.env.NODE_ENV === "production" ? "error" : "off",
    quotes: ["error", "double"],
    semi: ["error", "always"],
    "comma-dangle": "off",
    "no-cond-assign": ["error", "always"],
    "space-before-function-paren": "off",
    indent: "off",
    "prettier/prettier": "off",
    "lines-between-class-members": "off",
  },
};
