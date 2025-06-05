import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

# Load dataset
file_path = "Dataset .csv"  # Replace with full path if needed
df = pd.read_csv(file_path)

# Task 1: Basic Exploration
# 1. Shape of dataset
num_rows, num_columns = df.shape
print(f"Number of rows: {num_rows}")
print(f"Number of columns: {num_columns}\n")

# 2. Missing values
missing_values = df.isnull().sum()
print("Missing values per column:\n", missing_values[missing_values > 0], "\n")

# 3. Data types
print("Data types:\n", df.dtypes, "\n")

# 4. Analyze target variable: Aggregate rating
rating_counts = df["Aggregate rating"].value_counts().sort_index()
rating_proportions = df["Aggregate rating"].value_counts(normalize=True).sort_index()

print("Aggregate rating distribution (counts):\n", rating_counts, "\n")
print("Aggregate rating distribution (proportions):\n", rating_proportions, "\n")

# Optional: Visualize the distribution
plt.figure(figsize=(10, 6))
sns.barplot(x=rating_counts.index, y=rating_counts.values, palette="viridis")
plt.title("Distribution of Aggregate Ratings")
plt.xlabel("Aggregate Rating")
plt.ylabel("Number of Restaurants")
plt.xticks(rotation=45)
plt.tight_layout()
plt.show()
