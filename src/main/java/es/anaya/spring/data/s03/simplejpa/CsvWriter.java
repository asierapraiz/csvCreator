package es.anaya.spring.data.s03.simplejpa;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import lombok.experimental.var;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

@Log4j
@Service
public class CsvWriter {

    @SneakyThrows
    public void createCsv(List<?> registros,  List<String> headers, String path) throws java.io.IOException {
        String dirPath = path + "/csvTest.csv";
        try(FileWriter writer = new FileWriter(dirPath)){
            //Pinto cabeceras
            Iterator<String> iterator = headers.iterator();
            while (iterator.hasNext()){
                writer.append(iterator.next());

                //Añado la coma mientras no sea el último valor
                if(iterator.hasNext()){
                    writer.append(",");
                }
            }
            writer.append("\n");
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

            //Aqui llamo a tu método pero algo no ve bien
            for (Object registro:registros){
                try{
                    convertirObjeto(registro);
                }catch (IllegalAccessException e){
                    System.out.println("Algo no ha ido bien");
                }
            }

            //Pinto los registros
            for(Object registro: registros){
                String s = ow.writeValueAsString(registro);
                StringBuilder sb = new StringBuilder(s);
                //elimino los corchetes
                sb.deleteCharAt(s.length()-1);
                sb.deleteCharAt(0);

                List<String> myList = new ArrayList<String>(Arrays.asList(sb.toString().split(",")));
                Integer size = myList.size();
                Integer counter = 0;
                for (String element: myList){
                    if(element.trim().equals(null)){
                        writer.append("");
                    }else if(!isNumeric(element)){
                        writer.append(element.trim());
                    }else if(isNumeric(element.trim())){
                        writer.append(",");
                    }

                    //Añado la coma mientras no sea el último valor
                    if(counter++ !=size-1){
                        writer.append(",");
                    }
                }
                writer.append("\n");
            }
            writer.flush();
            writer.close();

            System.out.println("csv  creado.");
        }catch (FileNotFoundException e){
            System.out.println("Error : "+ e.getMessage());
        }
    }

    public boolean isNumeric(String strNum){
        Pattern isNumericPattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if(strNum == null){
            return false;
        }
        return isNumericPattern.matcher(strNum).matches();
    }

    public static void convertirObjeto(Object obj) throws IllegalArgumentException, IllegalAccessException
    {

        Class<?> clase = obj.getClass();
        // SACAMOS EL LISTADO DE LOS CAMPOS
        Field[] campos = clase.getDeclaredFields();

        // RECORREMOS LOS CAMPOS, AQUI VALUE YA TE LO DUELVE SIN COMILLAS
        for (Field field : campos) {
            Object value = field.get(obj);

            System.out.println("Tipo :"+value.getClass().getName());

            if (value.getClass().getName().equals("java.util.Date"))
            {
                System.out.println(field.getName() + " (Fecha) =" + value);
            }
            else if (value.getClass().getName().equals("java.lang.String"))
            {
                System.out.println(field.getName()+ " (String) ="+ value);
            }
            else if (value != null) {
                System.out.println(field.getName() + "=" + value);
            }
        }
    }

    /*
    Esto es un ejemplo de como lo hicieron en otra aplcación con c#, usa un reader que lo hace muy fácil
    No hay algo asi en java?

    private void CargarDatos(string municipioConsulta, ConsultasCSV consultaCSV)
        {
            try
            {
                requisitos.Output.AddMessage("[" + consultaCSV.nombre.ToUpper() + "] Comienzo carga extarchivo por municipio: " + municipioConsulta);
                List<string> lineas = new List<string>();
                string consulta = consultaCSV.consulta + "'" + municipioConsulta + "'";
                using (NpgsqlCommand command = new NpgsqlCommand(consulta, requisitos.Conexion))
                using (NpgsqlDataReader reader = command.ExecuteReader())
                {
                    string lineaDatos = string.Empty;
                    var columns = Enumerable.Range(0, reader.FieldCount).Select(reader.GetName).ToList();
                    foreach (var column in columns)
                    {
                        if (lineaDatos.Equals(string.Empty))
                        {
                            lineaDatos += column.ToString();
                        }
                        else
                        {
                            lineaDatos += SEPARADOR + column.ToString();
                        }
                    }
                    lineas.Add(lineaDatos);
                    while (reader.Read())
                    {
                       var valores = new List<string>();
                        for (int i = 0; i < reader.FieldCount; i++)
                        {
                            if (reader.GetFieldType(i) == typeof(String))
                                valores.Add("\"" + Conv.AStringTrim(reader[i]).Replace("\n", " ").Replace("\"", "'") + "\"");
                                //valores.Add(Conv.AStringTrim(reader[i]).Replace("\n", " "));
                            else
                                valores.Add(Conv.AStringTrim(reader[i]));
                        }
                        lineas.Add(string.Join(SEPARADOR, valores.ToArray()));
                    }
                    string rutaCompleta = RutaDirectorio(municipioConsulta) + "\\" + consultaCSV.nombre;
                    Guardar(rutaCompleta, lineas);
                }
            }
            catch (Exception ex)
            {
                throw ex;
            }
        }


*/

}
