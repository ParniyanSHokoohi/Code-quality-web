 # Xpaths of elements to work with
#from grp import struct_group
from selenium import webdriver
from selenium.common.exceptions import NoSuchElementException
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.chrome.options import Options  
from time import sleep 
import json
import re
from sympy import EX

def read_atributes(repo_url):

    secs_to_wait = 1

    ##github url von vue
    #github_vue_url = "https://github.com/search?l=JavaScript&q=online+shop+vue&type=Repositories" 

    #github_vue_url= "https://github.com/BosNaufal/vue-mini-shop"
    ## css selector of element that we need.
    code_button_selector = '#repo-content-pjax-container > div > div > div.Layout.Layout--flowRow-until-md.Layout--sidebarPosition-end.Layout--sidebarPosition-flowRow-end > div.Layout-main > div.file-navigation.mb-3.d-flex.flex-items-start > span > get-repo > feature-callout > details > summary'
    linktorepo_selector = '<input type="text" class="form-control input-monospace input-sm color-bg-subtle" data-autoselect="" value="https://github.com/ParniyanSHokoohi/code-quality-web.git" aria-label="https://github.com/ParniyanSHokoohi/code-quality-web.git" readonly="">'
    linktorepo_attribute_value ='value'
    #text= ['strars','Forks','commit','collabration','watching','branches','issues','pull requests','contributors'
    watch_fork_star_selctor ="#repo-content-pjax-container > div > div > div.Layout.Layout--flowRow-until-md.Layout--sidebarPosition-end.Layout--sidebarPosition-flowRow-end > div.Layout-sidebar > div > div.BorderGrid-row.hide-sm.hide-md > div"
    n_commits_selector ="#repo-content-pjax-container > div.clearfix.container-xl.px-3.px-md-4.px-lg-5.mt-4 > div > div.Layout.Layout--flowRow-until-md.Layout--sidebarPosition-end.Layout--sidebarPosition-flowRow-end > div.Layout-main > div.Box.mb-3 > div.Box-header.position-relative > div > div:nth-child(4) > ul > li > a > span"
    n_issues_selector ="#issues-tab"
    n_breanches_selector= "#repo-content-pjax-container > div > div > div.Layout.Layout--flowRow-until-md.Layout--sidebarPosition-end.Layout--sidebarPosition-flowRow-end > div.Layout-main > div.file-navigation.mb-3.d-flex.flex-items-start > div.flex-self-center.ml-3.flex-self-stretch.d-none.d-lg-flex.flex-items-center.lh-condensed-ultra"
    n_pullrequests_selector = '#pull-requests-tab'
    n_contributors_selector = "#repo-content-pjax-container > div > div > div.Layout.Layout--flowRow-until-md.Layout--sidebarPosition-end.Layout--sidebarPosition-flowRow-end > div.Layout-sidebar > div > div:nth-child(5) > div > h2 > a"



    # Start the browser
    options = Options()
    # set headless option to True to run browser in headless mode
    options.headless = True

    browser = webdriver.Chrome(options=options)

    # Go to the github_vue website
    browser.get(repo_url)### get url github_vue webseite
    sleep(secs_to_wait)# mach eine Pause.## wir machen pause weil normale mensh mach pause sonst ohne Pause sie merken das ist eine Robat ist.


    #find elemet watch-folk
    star_watch_folk_element = browser.find_element(By.CSS_SELECTOR, watch_fork_star_selctor)
    watch_folk_text = star_watch_folk_element.get_attribute('textContent')
    #print(watch_folk_text)

    n_commits_element = browser.find_element(By.CSS_SELECTOR,n_commits_selector)
    n_commits_text = n_commits_element.get_attribute('textContent')
    #print(n_commits_text)

    n_issues_element = browser.find_element(By.CSS_SELECTOR,n_issues_selector)
    n_issues_text = n_issues_element.get_attribute('textContent')


    n_breanches_element = browser.find_element(By.CSS_SELECTOR,n_breanches_selector)
    n_breanches_text = n_breanches_element.get_attribute('textContent')


    n_pullrequests_element = browser.find_element(By.CSS_SELECTOR,n_pullrequests_selector)
    n_pullrequests_text = n_pullrequests_element.get_attribute('textContent') 


    try:
        n_contributors_element = browser.find_element(By.CSS_SELECTOR,n_contributors_selector)
        n_contributors_text = n_contributors_element.get_attribute('textContent')
    except Exception as e:
        print(e)
        n_contributors_text = '1'



    atribute_name = ['stars','watching','forks','commits','branches']
    atribute_dict={}
    text = watch_folk_text+n_commits_text+n_breanches_text
    for atribute   in  atribute_name:
        s=rf'(?P<n_{atribute}>\d+)\s*{atribute}'
        X = re.search(s, text)
        print(s)

        atribute_dict[atribute]= X.group(f'n_{atribute}')


    s = re.search(r'(?P<issues>\d+)',n_issues_text)
    p = re.search(r'(?P<Pullrequests>\d+)',n_pullrequests_text)
    c = re.search(r'(?P<contributors>\d+)',n_contributors_text)

    atribute_dict['issues'] = s.group('issues')
    atribute_dict['pull requests'] = p.group('Pullrequests')
    atribute_dict['contributors'] = c.group('contributors')

    
    browser.close()
    return(atribute_dict)


github_vue_url= "https://github.com/BosNaufal/vue-mini-shop" 
print(read_atributes(github_vue_url))