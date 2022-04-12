HealthAsyst Hybrid Automation Framework.
Created by HA Quality Team.
========================================================================================================
Version 1.0 (16-Jan-2017) - Upgrade Selenium 3.0.1
========================================================================================================
Supports:
+ Browsers: FF (Lasted version); Chrome (Lasted version); IE (10;11)
+ Apache Log 1.2.17
+ Apache POI 3.14
+ Selenium 3.0.1
+ Tested on Windows 10.
+ Excel 2003 - 2016. Report: Summary report and charts.
+ Exception handling: Take screenshot for failed steps.
+ Object repositories be collected following by XPATH
========================================= 41 Keywords ===================================================
Here is the list of Keyword that current framework is supporting:

    - openBrowser : Chrome, FF, IE. Default browser: FF ~ FireFox
    - navigateToURL : Set the browser to go to URL (Defined on OR ~ Object Repository file)
    - navigateTo : Go to the URL (Define on data test column on Excel file)
    - clickElement  
     - clickElementByLinkText                            - moveToElement
    - closeBrowser                              - doubleClick
    - clearTextBox                              - navigateToForward
    - waitAndPause                              - navigateToBack
    - inputValue                                - submit
    - refreshPage                               - clearTextBox
    - doubleClick                               - waitForElementPresent
    - verifyElementIsExisted                    - verifyText
    - closeAllBrowsers                          - waitForAjax
    - selectByVisibleText                       - selectByValue
    - selectByIndex                             - switchToIFrameWithID
    - switchToIFrameWithName                    - switchToMainPage
    - verifyTextInTable
    - verifyCheckboxIsChecked                   - verifyCheckboxIsNotChecked
    - verifyRadioIsChecked                      - verifyRadioIsNotChecked
    - checkCheckBox                             - unCheckCheckBox
    - clickTreeViewItem                         - verifyTreeViewItemExist
    - verifyTreeViewItemNotExist
    - closeAlertOfBrowser                       - acceptAlertOfBrowser
    - verifyAlertText                           - sendValueToAlert
======================================= Exception Handling =============================================
Get screenshot
    + To take screenshots for failed steps on a test cases.
    + testing.reports file be named following naming convention.
    + Error__[TestCase]__[TestStep] (Eg: Error__TestCase1__TS_003__Mon Nov 14 11-47-40 ICT 2016.png)
Action log
    All action and exception cases will be saved with log formats, supported by Apache Log 1.2.
========================================================================================================