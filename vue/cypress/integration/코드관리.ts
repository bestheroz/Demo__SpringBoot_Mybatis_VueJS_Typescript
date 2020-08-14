before(() => {
  cy.login(Cypress.env('username'), Cypress.env('password'));
  cy.saveLocalStorage();
});

beforeEach(() => {
  cy.restoreLocalStorage();
});

describe('관리자>코드관리', () => {
  it('화면이동', () => {
    cy.server();
    cy.route('**/api/admin/codeGroups/').as('getList');
    cy.menu('관리자', '코드관리');
    cy.wait('@getList');
  });
  it('코드그룹추가 - TEST_CODE_GROUP', () => {
    cy.server();
    cy.route('**/api/admin/codeGroups/').as('save');
    cy.get('div.v-speed-dial:eq(0)').trigger('mouseenter');
    cy.get(
      'div.v-speed-dial:eq(0) div.v-speed-dial__list button:eq(2)',
    ).click();
    cy.get('div.v-dialog__content--active').within(() => {
      cy.get('label').contains('그룹코드').next().type('TEST_CODE_GROUP');
      cy.get('label').contains('그룹코드명').next().type('테스트 코드 그룹');
      cy.get('button').contains('저장').click();
    });
    cy.wait('@save');
    cy.wait(20).get('button').contains('성공').click();
  });
  it('코드추가 - 하위코드1', () => {
    cy.server();
    cy.route('**/api/admin/codes/**').as('save');
    cy.get('td>a')
      .contains('TEST_CODE_GROUP')
      .parent()
      .prev()
      .children('div.v-simple-checkbox')
      .click();
    cy.get('div.v-speed-dial:eq(1)').trigger('mouseenter');
    cy.get(
      'div.v-speed-dial:eq(1) div.v-speed-dial__list button:eq(2)',
    ).click();

    cy.get('div.v-dialog__content--active').within(() => {
      cy.get('label').contains('사용안함').click();
      cy.get('label').contains('상세 코드').next().type('TEST_CODE_1');
      cy.get('label').contains('상세 코드명').next().type('테스트 하위코드1');
      cy.chooseSelectValue('권한', '마스터 개발자', true);
      cy.get('label').contains('정렬순서').next().type('9991');
      cy.get('button').contains('저장').click();
    });
    cy.wait('@save');
    cy.wait(20).get('button').contains('성공').click();
  });
  it('코드추가 - 하위코드2', () => {
    cy.server();
    cy.route('**/api/admin/codes/**').as('save');
    cy.get('div.v-speed-dial:eq(1)').trigger('mouseenter');
    cy.get(
      'div.v-speed-dial:eq(1) div.v-speed-dial__list button:eq(2)',
    ).click();

    cy.get('div.v-dialog__content--active').within(() => {
      cy.get('label').contains('사용안함').click();
      cy.get('label').contains('상세 코드').next().type('TEST_CODE_2');
      cy.get('label').contains('상세 코드명').next().type('테스트 하위코드2');
      cy.chooseSelectValue('권한', '마스터 개발자', true);
      cy.get('label').contains('정렬순서').next().type('9992');
      cy.get('button').contains('저장').click();
    });
    cy.wait('@save');
    cy.wait(20).get('button').contains('성공').click();
  });
  it('코드수정 - 코드 그룹', () => {
    cy.server();
    cy.route('**/api/admin/codeGroups/**').as('save');
    cy.get('tr>td>a').contains('TEST_CODE_GROUP').click();
    cy.get('div.v-dialog__content--active').within(() => {
      cy.get('label')
        .contains('그룹코드명')
        .next()
        .type('테스트 코드 그룹_수정테스트');
      cy.get('button').contains('저장').click();
    });
    cy.wait('@save');
    cy.wait(20).get('button').contains('성공').click();
  });
  it('코드수정 - 하위코드1', () => {
    cy.server();
    cy.route('**/api/admin/codes/**').as('save');
    cy.get('td>a')
      .contains('TEST_CODE_GROUP')
      .parent()
      .prev()
      .children('div.v-simple-checkbox')
      .click();
    cy.get('tr>td>a').contains('TEST_CODE_1').click();
    cy.get('div.v-dialog__content--active').within(() => {
      cy.get('label')
        .contains('상세 코드명')
        .next()
        .clear()
        .type('테스트 하위코드1111');
      cy.chooseSelectValue('권한', '마스터 개발자', true);
      cy.get('label').contains('정렬순서').next().clear().type('9992');
      cy.get('button').contains('저장').click();
    });
    cy.wait('@save');
    cy.wait(20).get('button').contains('성공').click();
  });
  it('수정된 코드확인', () => {
    cy.get('tr>td').should('contain', '테스트 하위코드1111');
  });
  it('코드수정 - MENU_TYPE > P', () => {
    cy.server();
    cy.route('**/api/admin/codes/**').as('save');
    cy.get('td>a')
      .contains('MENU_TYPE')
      .parent()
      .prev()
      .children('div.v-simple-checkbox')
      .click();
    cy.get('tr>td').contains('페이지').prev().children('a').click();
    cy.get('div.v-dialog__content--active').within(() => {
      cy.get('label')
        .contains('상세 코드명')
        .next()
        .clear()
        .type('페이쥐_테스트');
      cy.get('button').contains('저장').click();
    });
    cy.wait('@save');
    cy.wait(20).get('button').contains('성공').click();
  });
  it('화면이동 - 변경된 값 확인(페이쥐 테스트)', () => {
    cy.server();
    cy.route('**/api/admin/menus/').as('getList');
    cy.menu('관리자', '메뉴관리');
    cy.wait('@getList');
    cy.get('tr>td').should('contain', '페이쥐_테스트');
  });
  it('코드수정 - MENU_TYPE > P - 복구', () => {
    cy.server();
    cy.route('**/api/admin/codes/**').as('save');
    cy.menu('관리자', '코드관리');
    cy.get('td>a')
      .contains('MENU_TYPE')
      .parent()
      .prev()
      .children('div.v-simple-checkbox')
      .click();
    cy.get('tr>td').contains('페이쥐_테스트').prev().children('a').click();
    cy.get('div.v-dialog__content--active').within(() => {
      cy.get('label').contains('상세 코드명').next().clear().type('페이지');
      cy.get('button').contains('저장').click();
    });
    cy.wait('@save');
    cy.wait(20).get('button').contains('성공').click();
  });
  it('원복 코드확인', () => {
    cy.get('tr>td').should('contain', '페이지');
  });
  it('코드삭제 - 하위코드2', () => {
    cy.server();
    cy.route('**/api/admin/codes/**').as('delete');
    cy.get('td>a')
      .contains('TEST_CODE_GROUP')
      .parent()
      .prev()
      .children('div.v-simple-checkbox')
      .click();
    cy.get('tr>td>a')
      .contains('TEST_CODE_2')
      .parent()
      .prev()
      .children('div.v-simple-checkbox')
      .click();
    cy.get('div.v-speed-dial:eq(1)').trigger('mouseenter');
    cy.get(
      'div.v-speed-dial:eq(1) div.v-speed-dial__list button:eq(1)',
    ).click();
    cy.get('button').contains('삭제 하겠습니다').click();
    cy.wait('@delete');
    cy.wait(20).get('button').contains('성공').click();
  });
  it('코드삭제 - 코드 그룹', () => {
    cy.server();
    cy.route('**/api/admin/codeGroups/**').as('delete');
    cy.get('tr>td>a')
      .contains('TEST_CODE_1')
      .parent()
      .prev()
      .children('div.v-simple-checkbox')
      .click();
    cy.get('div.v-speed-dial:eq(0)').trigger('mouseenter');
    cy.get(
      'div.v-speed-dial:eq(0) div.v-speed-dial__list button:eq(1)',
    ).click();
    cy.get('button').contains('삭제 하겠습니다').click();
    cy.wait('@delete');
    cy.wait(20).get('button').contains('성공').click();
  });
});
