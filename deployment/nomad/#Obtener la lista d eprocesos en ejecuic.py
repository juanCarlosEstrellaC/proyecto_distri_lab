#Obtener la lista d eprocesos en ejecuicion
import psutil # type: ignore
procesos = psutil.process_iter(attrs = ['pid','name','status'])
print ("Lista de procesos en ejecucucion:")
for proceso in procesos:
    print(proceso.info)
    
    
    
import subprocess
#iniciar el bloc de notas como un nuevo proceso
proceso = subprocess.Popen(["notepad.exe"])
print(f"Seha iniciado el proceso con PID: {proceso.pid}")


import subprocess
#iniciar el bloc de notas como un nuevo proceso
proceso1 = subprocess.Popen(["calc.exe"])
print(f"Seha iniciado el proceso con PID: {proceso1.pid}")



import psutil

pid_a_finalizar = int(input("Ingre el PID del proceso a cerrar: "))
try: 
    proceso = psutil.Process(pid_a_finalizar)
    proceso.terminate() #termina el proceso
    print(f"El proceso con PID {pid_a_finalizar} ha sido finalizado.")
except psutil.NoSuchProcess: print("El proceso no existe")

