module.exports = {
  root: true,
  parserOptions: {
    parser: '@typescript-eslint/parser',
  },
  env: {
    browser: true,
    node: true,
    mocha: true,
    es6: true
  },
  globals: {
    expect: true
  },
  extends: ['plugin:vue/essential', '@vue/prettier', '@vue/typescript'],
  plugins: ["vue"],
  rules: {
    'no-console': 'off',
    "generator-star-spacing": "off",
    "no-debugger": process.env.NODE_ENV === "production" ? "error" : "off"
  }
};
