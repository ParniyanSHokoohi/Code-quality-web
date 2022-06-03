 # Xpaths of elements to work with
from selenium import webdriver
from selenium.common.exceptions import NoSuchElementException
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.chrome.options import Options  
from time import sleep 
import json
import re
  
secs_to_wait = 1

##github url von vue
#github_vue_url = "https://github.com/search?l=JavaScript&q=online+shop+vue&type=Repositories" 

github_vue_url= "https://github.com/BosNaufal/vue-mini-shop"
## css selector of element that we need.
code_button_selector = '#repo-content-pjax-container > div > div > div.Layout.Layout--flowRow-until-md.Layout--sidebarPosition-end.Layout--sidebarPosition-flowRow-end > div.Layout-main > div.file-navigation.mb-3.d-flex.flex-items-start > span > get-repo > feature-callout > details > summary'
linktorepo_selector = '<input type="text" class="form-control input-monospace input-sm color-bg-subtle" data-autoselect="" value="https://github.com/ParniyanSHokoohi/code-quality-web.git" aria-label="https://github.com/ParniyanSHokoohi/code-quality-web.git" readonly="">'
linktorepo_attribute_value ='value'
#text= ['strars','Forks','commit','collabration','watching','branches','issues','pull requests','contributors'
watch_fork_star_selctor ="#repo-content-pjax-container > div > div > div.Layout.Layout--flowRow-until-md.Layout--sidebarPosition-end.Layout--sidebarPosition-flowRow-end > div.Layout-sidebar > div > div.BorderGrid-row.hide-sm.hide-md > div"

browser = webdriver.Chrome()

# Go to the github_vue website
browser.get(github_vue_url)### get url github_vue webseite
sleep(secs_to_wait)# mach eine Pause.## wir machen pause weil normale mensh mach pause sonst ohne Pause sie merken das ist eine Robat ist.


# find elemet watch-folk
star_watch_folk_element = browser.find_element(By.CSS_SELECTOR, watch_fork_star_selctor)
   
watch_folk_text = star_watch_folk_element.get_attribute('textContent')

print(watch_folk_text)


atribute_name = ['stars','watching','forks']
atribute_dict={}

for atribute   in  atribute_name:
    s=rf'(?P<n_{atribute}>\d+)\s*{atribute}'
    X = re.search(s, watch_folk_text)
    print(s)

    atribute_dict[atribute]= X.group(f'n_{atribute}')


print(atribute_dict)

browser.close()
    