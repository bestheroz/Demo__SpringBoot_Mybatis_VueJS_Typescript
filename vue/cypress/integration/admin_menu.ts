before(() => {
  // @ts-ignore
  cy.login();
  cy.saveLocalStorage();
});

beforeEach(() => {
  cy.restoreLocalStorage();
});

describe('관리자>메뉴관리', () => {
  it('화면이동', () => {
    cy.server();
    cy.visit('/admin/menu');
  });
  it('메뉴추가', () => {
    cy.contains('하위메뉴입력').eq(0).click();
  });
});
