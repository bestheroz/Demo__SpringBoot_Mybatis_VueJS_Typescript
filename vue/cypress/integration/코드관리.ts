before(() => {
  cy.login();
  cy.saveLocalStorage();
});

beforeEach(() => {
  cy.restoreLocalStorage();
});

describe('관리자>코드관리', () => {
  it('화면이동', () => {
    cy.server();
    cy.route('**/api/admin/codeGroups/').as('getList');
    cy.visit('/admin/code');
    cy.wait('@getList');
  });
  // it('코드그룹추가 - TEST_CODE_GROUP', () => {
  //   cy.server();
  //   cy.route('**/api/admin/codeGroups/').as('save');
  //   cy.get('div.v-speed-dial:eq(0)').trigger('mouseenter');
  //   cy.get('div.v-speed-dial:eq(0) div.v-speed-dial__list button:eq(2)').click();
  //   cy.get('div.v-dialog__content--active').within(() => {
  //     cy.get('label').contains('그룹코드').next().type('TEST_CODE_GROUP');
  //     cy.get('label').contains('그룹코드명').next().type('테스트 코드 그룹');
  //     cy.get('button').contains('저장').click();
  //   });
  //   cy.wait('@save');
  //   cy.get('button').contains('성공').click();
  // });
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
    cy.get('button').contains('성공').click();
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
    cy.get('button').contains('성공').click();
  });
  // it('추가된 메뉴 - 권한 추가', () => {
  //   cy.server();
  //   cy.route('**/api/codes/AUTHORITY').as('AUTHORITY');
  //   cy.route('**/api/admin/menuAuthority/**').as('getList');
  //   cy.menu('관리자', '메뉴권한관리');
  //   cy.wait('@AUTHORITY');
  //   cy.chooseSelectValue('권한 선택', '마스터 관리자');
  //   cy.wait('@getList');
  //   cy.get('label').contains('TEST_CODE_GROUP').click();
  //   cy.get('label').contains('테스트 하위코드1').click();
  //   cy.get('label').contains('테스트 하위코드2').click();
  //   cy.get('div.v-speed-dial').trigger('mouseenter');
  //   cy.get('div.v-speed-dial div.v-speed-dial__list button:eq(1)').click();
  // });
  // it('추가된 메뉴확인', () => {
  //   cy.visitHome();
  //   cy.get('nav.v-navigation-drawer').within(() => {
  //     cy.get('div.v-list-item__title').contains('TEST_CODE_GROUP').click();
  //     cy.get('div.v-list-item__title').contains('테스트 하위코드1');
  //     cy.get('div.v-list-item__title').contains('테스트 하위코드2');
  //   });
  // });
  // it('메뉴수정 - 그룹메뉴', () => {
  //   cy.server();
  //   cy.route('**/api/admin/codes/**').as('save');
  //   cy.get('tr>td>span')
  //     .contains('TEST_CODE_GROUP')
  //     .parent('td')
  //     .next('td')
  //     .next('td')
  //     .children('button')
  //     .contains('수정')
  //     .click();
  //   cy.get('div.v-dialog__content--active').within(() => {
  //     cy.get('label')
  //       .contains('메뉴명')
  //       .next()
  //       .clear()
  //       .type('TEST_CODE_GROUP0000');
  //     cy.get('label').contains('메뉴 순서').next().clear().type('99901');
  //     cy.get('button').contains('저장').click();
  //   });
  //   cy.wait('@save');
  //   cy.get('button').contains('성공').click();
  // });
  // it('메뉴수정 - 하위코드1', () => {
  //   cy.server();
  //   cy.route('**/api/admin/codes/**').as('save');
  //   cy.get('tr>td>span')
  //     .contains('테스트 하위코드1')
  //     .parent('td')
  //     .next('td')
  //     .next('td')
  //     .children('button')
  //     .contains('수정')
  //     .click();
  //   cy.get('div.v-dialog__content--active').within(() => {
  //     cy.get('label')
  //       .contains('메뉴명')
  //       .next()
  //       .clear()
  //       .type('테스트 하위코드1111');
  //     cy.get('label').contains('링크 URL').next().clear().type('/test11');
  //     cy.get('label').contains('메뉴 순서').next().clear().type('99955');
  //     cy.get('button').contains('저장').click();
  //   });
  //   cy.wait('@save');
  //   cy.get('button').contains('성공').click();
  // });
  // it('메뉴수정 - 하위코드2', () => {
  //   cy.server();
  //   cy.route('**/api/admin/codes/**').as('save');
  //   cy.get('tr>td>span')
  //     .contains('테스트 하위코드2')
  //     .parent('td')
  //     .next('td')
  //     .next('td')
  //     .children('button')
  //     .contains('수정')
  //     .click();
  //   cy.get('div.v-dialog__content--active').within(() => {
  //     cy.get('label')
  //       .contains('메뉴명')
  //       .next()
  //       .clear()
  //       .type('테스트 하위코드2222');
  //     cy.get('label').contains('링크 URL').next().clear().type('/test22');
  //     cy.get('label').contains('메뉴 순서').next().clear().type('99966');
  //     cy.get('button').contains('저장').click();
  //   });
  //   cy.wait('@save');
  //   cy.get('button').contains('성공').click();
  // });
  // it('수정된 메뉴확인', () => {
  //   cy.visitHome();
  //   cy.get('nav.v-navigation-drawer').within(() => {
  //     cy.get('div.v-list-item__title').contains('TEST_CODE_GROUP0000').click();
  //     cy.get('div.v-list-item__title').contains('테스트 하위코드1111');
  //     cy.get('div.v-list-item__title').contains('테스트 하위코드2222');
  //   });
  // });
  // it('화면이동', () => {
  //   cy.server();
  //   cy.route('**/api/admin/codes/').as('getList');
  //   cy.visit('/admin/menu');
  //   cy.wait('@getList');
  // });
  // it('메뉴삭제 - 하위코드2', () => {
  //   cy.server();
  //   cy.route('**/api/admin/codes/**').as('delete');
  //   cy.get('tr>td>span')
  //     .contains('테스트 하위코드2')
  //     .parent('td')
  //     .next('td')
  //     .next('td')
  //     .children('button')
  //     .contains('삭제')
  //     .click();
  //   cy.get('button').contains('삭제 하겠습니다').click();
  //   cy.wait('@delete');
  //   cy.get('button').contains('성공').click();
  // });
  // it('메뉴삭제 - 하위코드1', () => {
  //   cy.server();
  //   cy.route('**/api/admin/codes/**').as('delete');
  //   cy.get('tr>td>span')
  //     .contains('테스트 하위코드1')
  //     .parent('td')
  //     .next('td')
  //     .next('td')
  //     .children('button')
  //     .contains('삭제')
  //     .click();
  //   cy.get('button').contains('삭제 하겠습니다').click();
  //   cy.wait('@delete');
  //   cy.get('button').contains('성공').click();
  // });
  // it('메뉴삭제 - 그룹메뉴', () => {
  //   cy.server();
  //   cy.route('**/api/admin/codes/**').as('delete');
  //   cy.get('tr>td>span')
  //     .contains('TEST_CODE_GROUP')
  //     .parent('td')
  //     .next('td')
  //     .next('td')
  //     .children('button')
  //     .contains('삭제')
  //     .click();
  //   cy.get('button').contains('삭제 하겠습니다').click();
  //   cy.wait('@delete');
  //   cy.get('button').contains('성공').click();
  // });
});
