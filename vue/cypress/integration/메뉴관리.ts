describe('관리자>메뉴관리, 관리자>메뉴권한관리', () => {
  before(() => {
    cy.login(Cypress.env('username'), Cypress.env('password'));
    cy.saveLocalStorage();
  });

  beforeEach(() => {
    cy.restoreLocalStorage();
  });

  it('화면이동', () => {
    cy.server();
    cy.route('GET', '**/api/admin/menus/').as('getList');
    cy.menu('관리자', '메뉴관리');
    cy.wait('@getList');
  });
  it('메뉴추가 - 그룹메뉴', () => {
    cy.server();
    cy.route('POST', '**/api/admin/menus/').as('save');
    cy.get('button').contains('하위메뉴입력').eq(0).click();
    cy.get('div.v-dialog__content--active').within(() => {
      cy.setInputValue('메뉴명', '(cypress)그룹메뉴');
      cy.setSelectValue('타입', '그룹');
      cy.setInputValue('메뉴 순서', '9990');
      cy.get('button').contains('저장').click();
    });
    cy.wait('@save');
    cy.clickAlert('성공');
  });
  it('메뉴추가 - 하위메뉴1', () => {
    cy.server();
    cy.route('POST', '**/api/admin/menus/').as('save');
    cy.get('td>span')
      .contains('(cypress)그룹메뉴')
      .last()
      .parent()
      .next()
      .next()
      .children('button')
      .contains('하위메뉴입력')
      .click();
    cy.get('div.v-dialog__content--active').within(() => {
      cy.setInputValue('메뉴명', '(cypress)하위메뉴1');
      cy.setSelectValue('타입', '페이지');
      cy.setInputValue('링크 URL', '/test1');
      cy.setInputValue('메뉴 순서', '9995');
      cy.get('button').contains('저장').click();
    });
    cy.wait('@save');
    cy.clickAlert('성공');
  });
  it('메뉴추가 - 하위메뉴2', () => {
    cy.server();
    cy.route('POST', '**/api/admin/menus/').as('save');
    cy.get('td>span')
      .contains('(cypress)그룹메뉴')
      .last()
      .parent()
      .next()
      .next()
      .children('button')
      .contains('하위메뉴입력')
      .click();
    cy.get('div.v-dialog__content--active').within(() => {
      cy.setInputValue('메뉴명', '(cypress)하위메뉴2');
      cy.setSelectValue('타입', '페이지');
      cy.setInputValue('링크 URL', '/test2');
      cy.setInputValue('메뉴 순서', '9996');
      cy.get('button').contains('저장').click();
    });
    cy.wait('@save');
    cy.clickAlert('성공');
  });
  it('추가된 메뉴 - 권한 추가', () => {
    cy.server();
    cy.route('GET', '**/api/codes/AUTHORITY').as('AUTHORITY');
    cy.route('GET', '**/api/admin/menuAuthority/900').as('getList');
    cy.route('PUT', '**/api/admin/menuAuthority/900/').as('save');
    cy.menu('관리자', '메뉴권한관리');
    cy.wait('@AUTHORITY');
    cy.setSelectValue('권한 선택', '마스터 관리자');
    cy.wait('@getList');
    cy.clickSelection('(cypress)그룹메뉴');
    cy.clickSelection('(cypress)하위메뉴1');
    cy.clickSelection('(cypress)하위메뉴2');
    cy.clickFunction(0, 1);
    cy.wait('@save');
    cy.clickAlert('성공');
  });
  it('추가된 메뉴확인', () => {
    cy.visitHome();
    cy.get('nav.v-navigation-drawer').within(() => {
      cy.get('div.v-list-item__title').contains('(cypress)그룹메뉴').click();
      cy.get('div.v-list-item__title').contains('(cypress)하위메뉴1');
      cy.get('div.v-list-item__title').contains('(cypress)하위메뉴2');
    });
  });
  it('메뉴수정 - 그룹메뉴', () => {
    cy.server();
    cy.route('GET', '**/api/admin/menus/').as('getList');
    cy.route('PATCH', '**/api/admin/menus/**').as('save');
    cy.menu('관리자', '메뉴관리');
    cy.wait('@getList');
    cy.get('tr>td>span')
      .contains('(cypress)그룹메뉴')
      .parent('td')
      .next('td')
      .next('td')
      .children('button')
      .contains('수정')
      .click();
    cy.get('div.v-dialog__content--active').within(() => {
      cy.setInputValue('메뉴명', '(cypress)그룹메뉴0000');
      cy.setInputValue('메뉴 순서', '99901');
      cy.get('button').contains('저장').click();
    });
    cy.wait('@save');
    cy.clickAlert('성공');
  });
  it('메뉴수정 - 하위메뉴1', () => {
    cy.server();
    cy.route('PATCH', '**/api/admin/menus/**').as('save');
    cy.get('tr>td>span')
      .contains('(cypress)하위메뉴1')
      .parent('td')
      .next('td')
      .next('td')
      .children('button')
      .contains('수정')
      .click();
    cy.get('div.v-dialog__content--active').within(() => {
      cy.setInputValue('메뉴명', '(cypress)하위메뉴1111');
      cy.setInputValue('링크 URL', '/test11');
      cy.setInputValue('메뉴 순서', '99955');
      cy.get('button').contains('저장').click();
    });
    cy.wait('@save');
    cy.clickAlert('성공');
  });
  it('메뉴수정 - 하위메뉴2', () => {
    cy.server();
    cy.route('PATCH', '**/api/admin/menus/**').as('save');
    cy.get('tr>td>span')
      .contains('(cypress)하위메뉴2')
      .parent('td')
      .next('td')
      .next('td')
      .children('button')
      .contains('수정')
      .click();
    cy.get('div.v-dialog__content--active').within(() => {
      cy.setInputValue('메뉴명', '(cypress)하위메뉴2222');
      cy.setInputValue('링크 URL', '/test22');
      cy.setInputValue('메뉴 순서', '99966');
      cy.get('button').contains('저장').click();
    });
    cy.wait('@save');
    cy.clickAlert('성공');
  });
  it('수정된 메뉴확인', () => {
    cy.visitHome();
    cy.get('nav.v-navigation-drawer').within(() => {
      cy.get('div.v-list-item__title')
        .contains('(cypress)그룹메뉴0000')
        .click();
      cy.get('div.v-list-item__title').contains('(cypress)하위메뉴1111');
      cy.get('div.v-list-item__title').contains('(cypress)하위메뉴2222');
    });
  });
  it('메뉴삭제 - 하위메뉴2', () => {
    cy.server();
    cy.route('DELETE', '**/api/admin/menus/**').as('delete');
    cy.route('GET', '**/api/admin/menus/').as('getList');
    cy.menu('관리자', '메뉴관리');
    cy.wait('@getList');
    cy.get('tr>td>span')
      .contains('(cypress)하위메뉴2')
      .parent('td')
      .next('td')
      .next('td')
      .children('button')
      .contains('삭제')
      .click();
    cy.get('button').contains('삭제 하겠습니다').click();
    cy.wait('@delete');
    cy.clickAlert('성공');
  });
  it('메뉴삭제 - 그룹메뉴', () => {
    cy.server();
    cy.route('DELETE', '**/api/admin/menus/**').as('delete');
    cy.get('tr>td>span')
      .contains('(cypress)그룹메뉴')
      .parent('td')
      .next('td')
      .next('td')
      .children('button')
      .contains('삭제')
      .click();
    cy.get('button').contains('삭제 하겠습니다').click();
    cy.wait('@delete');
    cy.clickAlert('성공');
  });
});
