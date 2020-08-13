before(() => {
  cy.login();
  cy.saveLocalStorage();
});

beforeEach(() => {
  cy.restoreLocalStorage();
});

describe('관리자>메뉴관리, 관리자>메뉴권한관리', () => {
  it('화면이동', () => {
    cy.server();
    cy.route('**/api/admin/menus/').as('getList');
    cy.visit('/admin/menu');
    cy.wait('@getList');
  });
  it('메뉴추가 - 그룹메뉴', () => {
    cy.server();
    cy.route('**/api/admin/menus/').as('save');
    cy.get('button').contains('하위메뉴입력').eq(0).click();
    cy.get('div.v-dialog__content--active').within(() => {
      cy.get('label').contains('메뉴명').next().type('테스트 그룹메뉴');
      cy.chooseSelectValue('타입', '그룹', true);
      cy.get('label').contains('메뉴 순서').next().type('9990');
      cy.get('button').contains('저장').click();
    });
    cy.wait('@save');
    cy.wait(20).get('button').contains('성공').click();
  });
  it('메뉴추가 - 하위메뉴1', () => {
    cy.server();
    cy.route('**/api/admin/menus/').as('save');
    cy.get('td>span')
      .contains('테스트 그룹메뉴')
      .last()
      .parent()
      .next()
      .next()
      .children('button')
      .contains('하위메뉴입력')
      .click();
    cy.get('div.v-dialog__content--active').within(() => {
      cy.get('label').contains('메뉴명').next().type('테스트 하위메뉴1');
      cy.chooseSelectValue('타입', '페이지', true);
      cy.get('label').contains('링크 URL').next().type('/test1');
      cy.get('label').contains('메뉴 순서').next().type('9995');
      cy.get('button').contains('저장').click();
    });
    cy.wait('@save');
    cy.wait(20).get('button').contains('성공').click();
  });
  it('메뉴추가 - 하위메뉴2', () => {
    cy.server();
    cy.route('**/api/admin/menus/').as('save');
    cy.get('td>span')
      .contains('테스트 그룹메뉴')
      .last()
      .parent()
      .next()
      .next()
      .children('button')
      .contains('하위메뉴입력')
      .click();
    cy.get('div.v-dialog__content--active').within(() => {
      cy.get('label').contains('메뉴명').next().type('테스트 하위메뉴2');
      cy.chooseSelectValue('타입', '페이지', true);
      cy.get('label').contains('링크 URL').next().type('/test2');
      cy.get('label').contains('메뉴 순서').next().type('9996');
      cy.get('button').contains('저장').click();
    });
    cy.wait('@save');
    cy.wait(20).get('button').contains('성공').click();
  });
  it('추가된 메뉴 - 권한 추가', () => {
    cy.server();
    cy.route('**/api/codes/AUTHORITY').as('AUTHORITY');
    cy.route('**/api/admin/menuAuthority/**').as('getList');
    cy.menu('관리자', '메뉴권한관리');
    cy.wait('@AUTHORITY');
    cy.chooseSelectValue('권한 선택', '마스터 관리자');
    cy.wait('@getList');
    cy.get('label').contains('테스트 그룹메뉴').click();
    cy.get('label').contains('테스트 하위메뉴1').click();
    cy.get('label').contains('테스트 하위메뉴2').click();
    cy.get('div.v-speed-dial').trigger('mouseenter');
    cy.get('div.v-speed-dial div.v-speed-dial__list button:eq(1)').click();
  });
  it('추가된 메뉴확인', () => {
    cy.visitHome();
    cy.get('nav.v-navigation-drawer').within(() => {
      cy.get('div.v-list-item__title').contains('테스트 그룹메뉴').click();
      cy.get('div.v-list-item__title').contains('테스트 하위메뉴1');
      cy.get('div.v-list-item__title').contains('테스트 하위메뉴2');
    });
  });
  it('메뉴수정 - 그룹메뉴', () => {
    cy.server();
    cy.route('**/api/admin/menus/**').as('save');
    cy.get('tr>td>span')
      .contains('테스트 그룹메뉴')
      .parent('td')
      .next('td')
      .next('td')
      .children('button')
      .contains('수정')
      .click();
    cy.get('div.v-dialog__content--active').within(() => {
      cy.get('label')
        .contains('메뉴명')
        .next()
        .clear()
        .type('테스트 그룹메뉴0000');
      cy.get('label').contains('메뉴 순서').next().clear().type('99901');
      cy.get('button').contains('저장').click();
    });
    cy.wait('@save');
    cy.wait(20).get('button').contains('성공').click();
  });
  it('메뉴수정 - 하위메뉴1', () => {
    cy.server();
    cy.route('**/api/admin/menus/**').as('save');
    cy.get('tr>td>span')
      .contains('테스트 하위메뉴1')
      .parent('td')
      .next('td')
      .next('td')
      .children('button')
      .contains('수정')
      .click();
    cy.get('div.v-dialog__content--active').within(() => {
      cy.get('label')
        .contains('메뉴명')
        .next()
        .clear()
        .type('테스트 하위메뉴1111');
      cy.get('label').contains('링크 URL').next().clear().type('/test11');
      cy.get('label').contains('메뉴 순서').next().clear().type('99955');
      cy.get('button').contains('저장').click();
    });
    cy.wait('@save');
    cy.wait(20).get('button').contains('성공').click();
  });
  it('메뉴수정 - 하위메뉴2', () => {
    cy.server();
    cy.route('**/api/admin/menus/**').as('save');
    cy.get('tr>td>span')
      .contains('테스트 하위메뉴2')
      .parent('td')
      .next('td')
      .next('td')
      .children('button')
      .contains('수정')
      .click();
    cy.get('div.v-dialog__content--active').within(() => {
      cy.get('label')
        .contains('메뉴명')
        .next()
        .clear()
        .type('테스트 하위메뉴2222');
      cy.get('label').contains('링크 URL').next().clear().type('/test22');
      cy.get('label').contains('메뉴 순서').next().clear().type('99966');
      cy.get('button').contains('저장').click();
    });
    cy.wait('@save');
    cy.wait(20).get('button').contains('성공').click();
  });
  it('수정된 메뉴확인', () => {
    cy.visitHome();
    cy.get('nav.v-navigation-drawer').within(() => {
      cy.get('div.v-list-item__title').contains('테스트 그룹메뉴0000').click();
      cy.get('div.v-list-item__title').contains('테스트 하위메뉴1111');
      cy.get('div.v-list-item__title').contains('테스트 하위메뉴2222');
    });
  });
  // it('화면이동 - 관리자>메뉴관리', () => {
  //   cy.server();
  //   cy.route('**/api/admin/menus/').as('getList');
  //   cy.menu('관리자', '메뉴관리');
  //   cy.wait('@getList');
  // });
  it('메뉴삭제 - 하위메뉴2', () => {
    cy.server();
    cy.route('**/api/admin/menus/**').as('delete');
    cy.get('tr>td>span')
      .contains('테스트 하위메뉴2')
      .parent('td')
      .next('td')
      .next('td')
      .children('button')
      .contains('삭제')
      .click();
    cy.get('button').contains('삭제 하겠습니다').click();
    cy.wait('@delete');
    cy.wait(20).get('button').contains('성공').click();
  });
  it('메뉴삭제 - 그룹메뉴', () => {
    cy.server();
    cy.route('**/api/admin/menus/**').as('delete');
    cy.get('tr>td>span')
      .contains('테스트 그룹메뉴')
      .parent('td')
      .next('td')
      .next('td')
      .children('button')
      .contains('삭제')
      .click();
    cy.get('button').contains('삭제 하겠습니다').click();
    cy.wait('@delete');
    cy.wait(20).get('button').contains('성공').click();
  });
});
