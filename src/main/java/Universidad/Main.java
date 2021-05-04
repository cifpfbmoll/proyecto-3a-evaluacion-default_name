package Universidad;

            Connection miConexion = obtenerConexion();
            Persona persona = new Persona();
            String[] datos = Persona.identificarse(miConexion);
            for(int i =0; i< datos.length; i++){
                System.out.println(datos[i]);
            }

            Administrador.verPersonas(miConexion);
        }


        public static Connection obtenerConexion () throws SQLException {
            String url = "jdbc:mysql://51.178.152.223:3306/gestion_universidad";
            return DriverManager.getConnection (url, "Dam4", "ProyectoGrupo4");
        }
    }
