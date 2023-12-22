# Waste Classification and Recycling Recommendation System

This project encompasses a waste classification system using Convolutional Neural Network (CNN) to identify types of waste based on provided images. Additionally, the project includes a recycling recommendation system based on the waste classification results.

**DATASET**
https://drive.google.com/drive/folders/1qixe-vhFxRZFcsnDXLx3AZOWTI6CYgoq?usp=sharing

## Process Workflow

1. **Data Preprocessing:**
   - The waste data is loaded and organized into a DataFrame using Python.
   - The data is split into train, validation, and test sets using `train_test_split`.

2. **Data Augmentation and Generators:**
   - Data augmentation on the train set is performed using Keras' `ImageDataGenerator`.
   - Data generators are created for each set (train, validation, test).

3. **Convolutional Neural Network (CNN) Construction:**
   - CNN is built using the MobileNetV2 pre-trained model as the base model.
   - Custom layers are added on top of the base model to form the CNN structure.

4. **Model Training:**
   - The model is trained using `model.fit` with train and validation data.
   - Model evaluation is conducted on test data to measure accuracy and loss.

5. **Fine-Tuning Model (Optional):**
   - Optional fine-tuning of the model is performed to enhance performance.

6. **Training Visualization:**
   - Learning curves (loss) for each epoch from train and validation data are visualized.

7. **Recommendation System Model:**
   - Recycling recommendation system for each waste class is prepared and stored in an accessible format for further use.

8. **Inference and Model Testing:**
   - The trained model is tested using various waste images for waste classification predictions and recycling recommendations.

9. **Model Conversion to TFLite:**
   - The trained and saved model is converted to the TFLite format for portability purposes.

[!TIP]
## How to Use
1. Download or clone this repository.
2. Ensure all necessary libraries and dependencies are installed.
3. Perform model training by running the provided code.
4. For model testing, use waste images for identification.

## Contributors
Nabila Salvaningtyas @sistina02

Chaesa Adella Rahma @chaesadella

---

Feel free to adapt the explanation according to the information and steps relevant to your project's structure and objectives.
