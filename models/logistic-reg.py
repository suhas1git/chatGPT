# Import necessary libraries
import pandas as pd
from sklearn.linear_model import LogisticRegression
from sklearn.model_selection import train_test_split

# Load the data
data = pd.read_csv('customer_data.csv')

# Split the data into training and testing sets
X_train, X_test, y_train, y_test = train_test_split(data[['Time_on_website', 'Pages_viewed', 'Purchased_before']], data['Purchased'], test_size=0.2, random_state=42)

# Create a logistic regression model and fit it to the training data
model = LogisticRegression()
model.fit(X_train, y_train)

# Evaluate the model on the testing data
accuracy = model.score(X_test, y_test)
print('Accuracy:', accuracy)

# Make predictions on new data
new_data = pd.DataFrame({'Time_on_website': [20, 40], 'Pages_viewed': [5, 10], 'Purchased_before': [0, 1]})
predictions = model.predict(new_data)
print('Predictions:', predictions)

############
# Time_on_website, Pages_viewed, Purchased_before, Purchased
# 35,6,1,1
# 45,8,1,1
# 60,10,0,1
# 20,4,0,0
# 50,9,1,1
# 30,5,1,0
# 55,12,0,1
# 25,6,0,0
