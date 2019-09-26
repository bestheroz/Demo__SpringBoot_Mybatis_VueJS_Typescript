module.exports = {
  root: true,
  parserOptions: {
    ecmaVersion: 6,
    parser: '@typescript-eslint/parser',
  },
  env: {
    browser: true,
    node: true,
    mocha: true,
    es6: true,
  },
  globals: {
    expect: true,
  },
  extends: [
    'eslint:recommended',
    'plugin:vue/essential',
    '@vue/prettier',
    '@vue/standard',
    '@vue/typescript',
  ],
  plugins: ['vue'],
  rules: {
    quotes: ['error', 'single', { allowTemplateLiterals: true }],
    'no-console': process.env.NODE_ENV === 'production' ? 'error' : 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'off',
    'vue/script-indent': ['error', 2, { baseIndent: 0, switchCase: 0 }],
    semi: [1, 'always'],
    'comma-dangle': ['error', 'only-multiline'],
    'space-before-function-paren': 'off',
    'no-unused-vars': 'off',
  },
};
