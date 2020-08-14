before(() => {
  cy.login(Cypress.env('username'), Cypress.env('password'));
  cy.saveLocalStorage();
});

beforeEach(() => {
  cy.restoreLocalStorage();
});

describe('관리자>회원관리', () => {
  it('화면이동', () => {
    cy.server();
    cy.route('**/api/admin/members/').as('getList');
    cy.menu('관리자', '회원관리');
    cy.wait('@getList');
  });
  // it('회원 추가 - tester', () => {
  //   cy.server();
  //   cy.route('**/api/admin/members/').as('save');
  //   cy.get('div.v-speed-dial:eq(0)').trigger('mouseenter');
  //   cy.get(
  //     'div.v-speed-dial:eq(0) div.v-speed-dial__list button:eq(2)',
  //   ).click();
  //   cy.get('div.v-dialog__content--active').within(() => {
  //     cy.get('label').contains('사용자아이디').next().type('tester');
  //     cy.get('label').contains('사용자명').next().type('테스터');
  //     cy.chooseSelectValue('권한', '손님', true);
  //     cy.get('label').contains('사용안함').click();
  //     cy.get('label').contains('세션타임아웃시간').next().clear().type('3000');
  //     cy.get('label').contains('비밀번호').next().type('tester1234');
  //     cy.get('label')
  //       .contains('비밀번호 확인')
  //       .next()
  //       .clear()
  //       .type('tester1234');
  //     cy.get('button').contains('저장').click();
  //   });
  //   cy.wait('@save');
  //   cy.wait(20).get('button').contains('성공').click();
  // });
  // it('로그인 및 세센타임아웃시간 체크 - tester', () => {
  //   cy.server();
  //   cy.login('tester', 'tester1234');
  //   cy.get('div.v-toolbar__content i.mdi-timer-sand')
  //     .parent()
  //     .should('contain', '00시간');
  //   cy.get('div.v-toolbar__content i.mdi-account')
  //     .parent()
  //     .should('contain', '테스터');
  //   cy.logout();
  // });
  // it('화면이동', () => {
  //   cy.server();
  //   cy.login(Cypress.env('username'), Cypress.env('password'));
  //   cy.saveLocalStorage();
  //   cy.route('**/api/admin/members/').as('getList');
  //   cy.menu('관리자', '회원관리');
  //   cy.wait('@getList');
  // });
  it('회원 수정 - tester', () => {
    cy.server();
    cy.route('**/api/admin/members/').as('save');
    cy.get('td>a').contains('tester').click();
    cy.get('div.v-dialog__content--active').within(() => {
      cy.get('div.v-input--selection-controls__input').click();
      cy.get('button').contains('저장').click();
    });
    cy.wait('@save');
    cy.wait(20).get('button').contains('성공').click();
  });
  const pbkdf2 = require('pbkdf2');
  it('로그인 실패 체크 - tester', () => {
    cy.server();
    cy.logout();
    cy.request({
      method: 'POST',
      url: `${Cypress.env('apiHost')}/api/auth/login`,
      body: {
        id: 'tester',
        password: pbkdf2
          .pbkdf2Sync('tester1234', 'salt', 1, 32, 'sha512')
          .toString(),
      },
    })
      .its('body')
      .then((body) => {
        console.log(body.data.code);
      });
  });
});
