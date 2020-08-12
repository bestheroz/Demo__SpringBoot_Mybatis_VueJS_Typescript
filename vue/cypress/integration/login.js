describe('로그인 체크', () => {
  it('로그인화면 이동', () => {
    cy.visit('http://127.0.0.1:8081');
  });

  it('아이디 패스워드 입력', () => {
    cy.get('input:eq(0)').type('1').should('have.value', '1');
    cy.get('input:eq(1)').type('1').should('have.value', '1');
  });

  it('로그인 처리', () => {
    cy.contains("Let's Go").click();
  });
});
