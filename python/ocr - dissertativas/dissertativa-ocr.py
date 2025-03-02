import pytesseract
from PIL import Image

#necessário: pip install pytesseract pillow
#necessário: sudo apt install tesseract-ocr tesseract-ocr-por
#no meu caso como rodo no wsl e o pycharm está no win, rodar pelo terminal python3 + nome arquivo.py
# Defina o caminho do executável do Tesseract se estiver no Windows
# Exemplo: pytesseract.pytesseract.tesseract_cmd = r"C:\Program Files\Tesseract-OCR\tesseract.exe"

# Caminho da imagem
imagem_path = "ocr - dissertativas/images.jpeg"

# Abrir a imagem
imagem = Image.open(imagem_path)

# Aplicar OCR (Português Brasil)
texto_extraido = pytesseract.image_to_string(imagem, lang="por")

# Exibir o resultado
print(texto_extraido)

# Opcional: salvar o texto em um arquivo
with open("texto_extraido.txt", "w", encoding="utf-8") as f:
    f.write(texto_extraido)
