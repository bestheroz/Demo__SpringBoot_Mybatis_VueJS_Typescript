describe('로그인/로그아웃', () => {
  it('로그인화면 이동', () => {
    cy.visit('/login');
  });

  it('아이디 패스워드 입력', () => {
    cy.get('input:eq(0)').type('1').should('have.value', '1');
    cy.get('input:eq(1)').type('1').should('have.value', '1');
  });

  it('로그인 처리', () => {
    cy.contains(' Go').click();
  });

  it('로그인 완료 확인 및 로그아웃', () => {
    cy.login(Cypress.env('username'), Cypress.env('password'));
    cy.get('div.v-toolbar__content i.mdi-timer-sand')
      .parent()
      .should('contain', '01시간');
    cy.logout();
  });
});
