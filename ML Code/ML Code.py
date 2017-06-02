# coding: utf-8

# In[1]:




# In[12]:

import pandas as pd


data = pd.read_excel('/Users/balaji/Documents/Pucharm projects new12/Workbook1.xlsx', sheetname='Sheet1')


# In[13]:

print(data)


# In[16]:

data.head()
X=data[['Temp','Wind','Humidity']]


# In[17]:

X.head


# In[18]:

y=data['Moisture']


# In[19]:

y.head


# In[25]:

from sklearn.ensemble import RandomForestRegressor
rfr = RandomForestRegressor(n_estimators=10)
rfr.fit(X,y, sample_weight=None)
X_test = [18,2,65]


# In[26]:

y_pred=rfr.predict(X_test)


# In[27]:

print(y_pred)


# In[23]:

type(y_pred)


# In[28]:

X_test.append(y_pred)


# In[29]:

print(X_test)
from openpyxl import load_workbook
wb = load_workbook("/Users/balaji/Documents/Pucharm projects new12/Workbook1.xlsx")
# You can also select a particular sheet
# based on sheet name
ws = wb.get_sheet_by_name("Sheet1")
last_row=ws.max_row
ws.cell(row=last_row,column=5).value = (y_pred[0])
wb.save("/Users/balaji/Documents/Pucharm projects new12/Workbook1.xlsx")


# In[ ]:
