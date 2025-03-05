from langchain_community.llms import Ollama

# Configurar o modelo do Ollama
modelo = Ollama(model="llama3.1:8b-instruct-q4_0")

# Teste
print(modelo.invoke("o que é um actuator java"))


