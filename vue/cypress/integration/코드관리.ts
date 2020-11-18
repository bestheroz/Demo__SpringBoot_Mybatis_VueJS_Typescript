describe("관리자>코드관리", () => {
  before(() => {
    cy.login(Cypress.env("username"), Cypress.env("password"));
    cy.saveLocalStorage();
  });

  beforeEach(() => {
    cy.restoreLocalStorage();
  });

  it("화면이동", () => {
    cy.server();
    cy.route("GET", "**/api/admin/codeGroups/").as("getList");
    cy.menu("관리자", "코드관리");
    cy.wait("@getList");
  });
  it("코드그룹추가 - (cypress)그룹코드", () => {
    cy.server();
    cy.route("POST", "**/api/admin/codeGroups/").as("save");
    cy.clickFunction(0, 2);
    cy.get("div.v-dialog__content--active").within(() => {
      cy.setInputAutoValue(["그룹코드", "그룹코드명"]);
      cy.get("button").contains("저장").click();
    });
    cy.wait("@save");
    cy.clickAlert("성공");
  });
  it("코드추가 - 하위코드1", () => {
    cy.server();
    cy.route("POST", "**/api/admin/codes/(cypress)그룹코드").as("save");
    cy.get("td>a")
      .contains("(cypress)그룹코드")
      .parent()
      .prev()
      .children("div.v-simple-checkbox")
      .click();
    cy.wait(200).clickFunction(1, 2);

    cy.get("div.v-dialog__content--active").within(() => {
      cy.setInputValue("상세 코드", "TEST_CODE_1");
      cy.setInputAutoValue(["상세 코드명"]);
      cy.setSelectValue("권한", "마스터 개발자");
      cy.setInputValue("정렬순서", "9991");
      cy.get("button").contains("저장").click();
    });
    cy.wait("@save");
    cy.clickAlert("성공");
  });
  it("코드추가 - 하위코드2", () => {
    cy.server();
    cy.route("POST", "**/api/admin/codes/(cypress)그룹코드").as("save");
    cy.clickFunction(1, 2);

    cy.get("div.v-dialog__content--active").within(() => {
      cy.setInputValue("상세 코드", "TEST_CODE_2");
      cy.setInputAutoValue(["상세 코드명"]);
      cy.setSelectValue("권한", "마스터 개발자");
      cy.setInputValue("정렬순서", "9992");
      cy.get("button").contains("저장").click();
    });
    cy.wait("@save");
    cy.clickAlert("성공");
  });
  it("코드수정 - 코드 그룹", () => {
    cy.server();
    cy.route("PATCH", "**/api/admin/codeGroups/(cypress)그룹코드").as("save");
    cy.get("tr>td>a").contains("(cypress)그룹코드").click();
    cy.get("div.v-dialog__content--active").within(() => {
      cy.setInputValue("그룹코드명", "(cypress)코드 그룹_수정");
      cy.get("button").contains("저장").click();
    });
    cy.wait("@save");
    cy.clickAlert("성공");
  });
  it("코드수정 - 하위코드1", () => {
    cy.server();
    cy.route("PATCH", "**/api/admin/codes/(cypress)그룹코드/TEST_CODE_1").as(
      "save",
    );
    cy.get("td>a")
      .contains("(cypress)그룹코드")
      .parent()
      .prev()
      .children("div.v-simple-checkbox")
      .click();
    cy.wait(200).get("tr>td>a").contains("TEST_CODE_1").click();
    cy.get("div.v-dialog__content--active").within(() => {
      cy.setInputValue("상세 코드명", "(cypress)하위코드1111");
      cy.setSelectValue("권한", "마스터 개발자");
      cy.setInputValue("정렬순서", "9992");
      cy.get("button").contains("저장").click();
    });
    cy.wait("@save");
    cy.clickAlert("성공");
  });
  it("수정된 코드확인", () => {
    cy.wait(100).get("tr>td").contains("(cypress)하위코드1111");
  });
  it("코드수정 - MENU_TYPE > P", () => {
    cy.server();
    cy.route("GET", "**/api/admin/codes/MENU_TYPE").as("getList");
    cy.route("PATCH", "**/api/admin/codes/MENU_TYPE/P").as("save");
    cy.get("td>a")
      .contains("MENU_TYPE")
      .parent()
      .prev()
      .children("div.v-simple-checkbox")
      .click();
    cy.wait("@getList")
      .wait(100)
      .get("tr>td")
      .contains("페이지")
      .prev()
      .children("a")
      .click();
    cy.wait(200)
      .get("div.v-dialog__content--active")
      .within(() => {
        cy.setInputValue("상세 코드명", "페이쥐_테스트");
        cy.get("button").contains("저장").click();
      });
    cy.wait("@save");
    cy.clickAlert("성공");
  });
  it("화면이동 - 변경된 값 확인(페이쥐 테스트)", () => {
    cy.server();
    cy.route("GET", "**/api/admin/menus/").as("getList");
    cy.wait(1000).menu("관리자", "메뉴관리");
    cy.wait("@getList");
    cy.wait(100).get("tr>td").contains("페이쥐_테스트");
  });
  it("코드수정 - MENU_TYPE > P - 복구", () => {
    cy.server();
    cy.route("GET", "**/api/admin/codes/MENU_TYPE").as("getList");
    cy.route("PATCH", "**/api/admin/codes/MENU_TYPE/P").as("save");
    cy.menu("관리자", "코드관리");
    cy.get("td>a")
      .contains("MENU_TYPE")
      .parent()
      .prev()
      .children("div.v-simple-checkbox")
      .click();
    cy.wait("@getList")
      .wait(100)
      .get("tr>td")
      .contains("페이쥐_테스트")
      .prev()
      .children("a")
      .click();
    cy.get("div.v-dialog__content--active").within(() => {
      cy.setInputValue("상세 코드명", "페이지");
      cy.get("button").contains("저장").click();
    });
    cy.wait("@save");
    cy.clickAlert("성공");
  });
  it("원복 코드확인", () => {
    cy.wait(100).get("tr>td").contains("페이지");
  });
  it("코드삭제 - 하위코드2", () => {
    cy.server();
    cy.route("GET", "**/api/admin/codes/(cypress)그룹코드").as("getList");
    cy.route("DELETE", "**/api/admin/codes/(cypress)그룹코드/TEST_CODE_2/").as(
      "delete",
    );
    cy.get("td>a")
      .contains("(cypress)그룹코드")
      .parent()
      .prev()
      .children("div.v-simple-checkbox")
      .click();
    cy.wait("@getList")
      .wait(100)
      .get("tr>td>a")
      .contains("TEST_CODE_2")
      .parent()
      .prev()
      .children("div.v-simple-checkbox")
      .click();
    cy.clickFunction(1, 1);
    cy.get("button").contains("삭제 하겠습니다").click();
    cy.wait("@delete");
    cy.clickAlert("성공");
  });
  it("코드삭제 - 코드 그룹", () => {
    cy.server();
    cy.route("GET", "**/api/admin/codes/(cypress)그룹코드").as("getList");
    cy.route("DELETE", "**/api/admin/codeGroups/(cypress)그룹코드").as(
      "delete",
    );
    cy.get("tr>td>a")
      .contains("TEST_CODE_1")
      .parent()
      .prev()
      .children("div.v-simple-checkbox")
      .click();
    cy.clickFunction(0, 1);
    cy.get("button").contains("삭제 하겠습니다").click();
    cy.wait("@delete");
    cy.clickAlert("성공");
  });
});
