module.exports = {
  root: true,
  parser: "vue-eslint-parser",
  parserOptions: {
    ecmaVersion: 2021,
  },
  env: {
    node: true,
  },
  extends: [
    "plugin:vue/strongly-recommended",
    "eslint:recommended",
    "@vue/typescript/recommended",
    "@vue/prettier",
    "@vue/standard",
    "@vue/prettier/@typescript-eslint",
  ],
  plugins: ["vue", "prettier"],
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
