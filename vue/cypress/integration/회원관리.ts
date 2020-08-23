before(() => {
  cy.login(Cypress.env('username'), Cypress.env('password'));
  cy.saveLocalStorage();
});

beforeEach(() => {
  cy.restoreLocalStorage();
});
const pbkdf2 = require('pbkdf2');
describe('관리자>회원관리', () => {
  it('화면이동 - 회원관리', () => {
    cy.server();
    cy.route('GET', '**/api/admin/members/').as('getList');
    cy.menu('관리자', '회원관리');
    cy.wait('@getList');
  });
  it('회원 추가 - tester', () => {
    cy.server();
    cy.route('POST', '**/api/admin/members/').as('save');
    cy.clickFunction(0, 2);
    cy.get('div.v-dialog__content--active').within(() => {
      cy.setInputValue('사용자아이디', 'tester')
        .setInputValue('사용자명', '테스터')
        .setSelectValue('권한', '손님')
        .setInputValue('세션타임아웃시간', '3000')
        .setInputValue('비밀번호', 'tester1234')
        .setInputValue('비밀번호 확인', 'tester1234');
      cy.get('button').contains('저장').click();
    });
    cy.wait('@save').clickAlert('성공');
  });
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
        cy.wrap(body).its('code').should('eq', 'F003');
      });
  });
  it('화면이동 - 회원관리', () => {
    cy.server();
    cy.login(Cypress.env('username'), Cypress.env('password'));
    cy.saveLocalStorage();
    cy.route('GET', '**/api/admin/members/').as('getList');
    cy.menu('관리자', '회원관리');
    cy.wait('@getList');
  });
  it('회원 수정 - tester', () => {
    cy.server();
    cy.route('PATCH', '**/api/admin/members/tester/').as('save');
    cy.get('td>a').contains('tester').click();
    cy.get('div.v-dialog__content--active').within(() => {
      cy.clickSelection('사용안함');
      cy.get('button').contains('저장').click();
    });
    cy.wait('@save').clickAlert('성공');
  });
  it('로그인 및 세센타임아웃시간 체크 - tester', () => {
    cy.server();
    cy.login('tester', 'tester1234');
    cy.get('div.v-toolbar__content i.mdi-timer-sand')
      .parent()
      .contains('00시간');
    cy.get('div.v-toolbar__content i.mdi-account').parent().contains('테스터');
    cy.logout();
  });
  it('화면이동', () => {
    cy.server();
    cy.login(Cypress.env('username'), Cypress.env('password'));
    cy.saveLocalStorage();
    cy.route('GET', '**/api/admin/members/').as('getList');
    cy.menu('관리자', '회원관리');
    cy.wait('@getList');
  });
  it('회원수정 - 비밀번호 초기화 - tester', () => {
    cy.server();
    cy.route('POST', '**/api/admin/members/tester/resetPassword').as(
      'resetPassword',
    );
    cy.route('PATCH', '**/api/admin/members/tester').as('save');
    cy.get('td>a').contains('tester').click();
    cy.get('div.v-dialog__content--active').within(() => {
      cy.get('button').contains('비밀번호 초기화').click();
      cy.wait('@resetPassword')
        .wait(4000)
        .get('button')
        .contains('저장')
        .click();
    });
    cy.wait('@save').clickAlert('성공');
  });
  it('로그인 및 비밀번호 초기화 - tester', () => {
    cy.server();
    cy.logout();
    cy.route('POST', '**/api/auth/initPassword').as('save');
    cy.route('POST', '**/api/auth/login').as('login');
    cy.get('input:eq(0)').type('tester').should('have.value', 'tester');
    cy.get('input:eq(1)').type('tester1234').should('have.value', 'tester1234');
    cy.contains(' Go').click();
    cy.wait('@login');
    cy.get('div.v-dialog--active').within(() => {
      cy.get('div.v-alert__content').contains('비밀번호 초기화');
      cy.setInputValue('비밀번호', 'tester1234').setInputValue(
        '비밀번호 확인',
        'tester1234',
      );
      cy.get('button').contains('저장').click();
    });
    cy.wait('@save')
      .get('input:eq(0)')
      .clear()
      .type('tester')
      .get('input:eq(1)')
      .type('tester1234');
    cy.contains(' Go').click();
    cy.wait('@login');
    cy.get('div.v-toolbar__content i.mdi-timer-sand')
      .parent()
      .contains('00시간');
    cy.get('div.v-toolbar__content i.mdi-account').parent().contains('테스터');
    cy.logout();
  });
  it('화면이동 - 회원관리', () => {
    cy.server();
    cy.login(Cypress.env('username'), Cypress.env('password'));
    cy.saveLocalStorage();
    cy.route('GET', '**/api/admin/members/').as('getList');
    cy.menu('관리자', '회원관리');
    cy.wait('@getList');
  });
  it('회원삭제 - tester', () => {
    cy.server();
    cy.route('DELETE', '**/api/admin/members/**').as('delete');
    cy.get('tr>td>a')
      .contains('tester')
      .parent()
      .prev()
      .children('div.v-simple-checkbox')
      .click();
    cy.clickFunction(0, 1);
    cy.get('button').contains('삭제 하겠습니다').click();
    cy.wait('@delete').clickAlert('성공');
  });
});
