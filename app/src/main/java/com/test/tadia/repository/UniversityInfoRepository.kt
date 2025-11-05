package com.test.tadia.repository

import com.test.tadia.data.InfoCategory
import com.test.tadia.data.UniversityInfo

/**
 * Repository for university information used by TadIA chatbot
 * This provides the knowledge base for answering user queries
 */
class UniversityInfoRepository {
    
    /**
     * Get all available university information
     * This is a hardcoded knowledge base - in production, this would come from Firestore
     */
    fun getAllUniversityInfo(): List<UniversityInfo> {
        return listOf(
            // Sala de Cine - Biblioteca
            UniversityInfo(
                id = "sala-cine",
                title = "Sala de Cine",
                description = "Sala especializada de cine ubicada en el cuarto piso de la biblioteca. Equipada para proyecciones audiovisuales y trabajo colaborativo.",
                category = InfoCategory.LIBRARY_SERVICES,
                location = "Biblioteca, Cuarto Piso",
                contactEmail = "biblioteca@utadeo.edu.co",
                requirements = listOf(
                    "Carnet estudiantil vigente",
                    "Reserva con 24 horas de anticipación",
                    "Mínimo 4 personas, máximo 10 personas",
                    "Uso exclusivo comunidad tadeísta"
                ),
                hours = "Lunes a Viernes: 7:00 AM - 9:00 PM, Sábados: 8:00 AM - 2:00 PM",
                availability = "Préstamo máximo 3 horas, renovable por 1 hora",
                additionalInfo = mapOf(
                    "capacity" to "4-10 personas",
                    "duration" to "Máximo 3 horas, renovable 1 hora",
                    "reservation" to "Formulario online mínimo 24h antes",
                    "waiting" to "Reserva respetada por 15 minutos"
                )
            ),
            
            // Sala de Idiomas - Biblioteca
            UniversityInfo(
                id = "sala-idiomas",
                title = "Sala de Idiomas",
                description = "Sala especializada para el estudio de idiomas en el tercer piso de la biblioteca. Espacio ideal para prácticas de conversación y aprendizaje de lenguas.",
                category = InfoCategory.LIBRARY_SERVICES,
                location = "Biblioteca, Tercer Piso",
                contactEmail = "biblioteca@utadeo.edu.co",
                requirements = listOf(
                    "Carnet estudiantil vigente",
                    "Reserva con 24 horas de anticipación",
                    "Mínimo 4 personas, máximo 10 personas",
                    "Uso exclusivo comunidad tadeísta"
                ),
                hours = "Lunes a Viernes: 7:00 AM - 9:00 PM, Sábados: 8:00 AM - 2:00 PM",
                availability = "Préstamo máximo 3 horas, renovable por 1 hora",
                additionalInfo = mapOf(
                    "capacity" to "4-10 personas",
                    "duration" to "Máximo 3 horas, renovable 1 hora",
                    "type" to "Trabajo colaborativo",
                    "purpose" to "Estudio y práctica de idiomas",
                    "reservation" to "Formulario online mínimo 24h antes"
                )
            ),
            
            // Sala del Comic - Biblioteca
            UniversityInfo(
                id = "sala-comic",
                title = "Sala del Comic",
                description = "Sala especializada de cómics ubicada en el tercer piso de la biblioteca. Espacio dedicado al estudio, lectura y apreciación de cómics y novelas gráficas.",
                category = InfoCategory.LIBRARY_SERVICES,
                location = "Biblioteca, Tercer Piso",
                contactEmail = "biblioteca@utadeo.edu.co",
                requirements = listOf(
                    "Carnet estudiantil vigente",
                    "Reserva con 24 horas de anticipación",
                    "Mínimo 4 personas, máximo 10 personas",
                    "Uso exclusivo comunidad tadeísta"
                ),
                hours = "Lunes a Viernes: 7:00 AM - 9:00 PM, Sábados: 8:00 AM - 2:00 PM",
                availability = "Préstamo máximo 3 horas, renovable por 1 hora",
                additionalInfo = mapOf(
                    "capacity" to "4-10 personas",
                    "duration" to "Máximo 3 horas, renovable 1 hora",
                    "type" to "Trabajo colaborativo",
                    "purpose" to "Lectura y estudio de cómics",
                    "reservation" to "Formulario online mínimo 24h antes"
                )
            ),
            
            // Sala Infantil - Biblioteca
            UniversityInfo(
                id = "sala-infantil",
                title = "Sala Infantil",
                description = "Sala infantil ubicada en el tercer piso de la biblioteca. Espacio dedicado para actividades con niños, cuentacuentos y talleres infantiles.",
                category = InfoCategory.LIBRARY_SERVICES,
                location = "Biblioteca, Tercer Piso",
                contactEmail = "biblioteca@utadeo.edu.co",
                requirements = listOf(
                    "Carnet estudiantil vigente",
                    "Reserva con 24 horas de anticipación",
                    "Mínimo 4 personas, máximo 10 personas",
                    "Uso exclusivo comunidad tadeísta"
                ),
                hours = "Lunes a Viernes: 7:00 AM - 9:00 PM, Sábados: 8:00 AM - 2:00 PM",
                availability = "Préstamo máximo 3 horas, renovable por 1 hora",
                additionalInfo = mapOf(
                    "capacity" to "4-10 personas",
                    "duration" to "Máximo 3 horas, renovable 1 hora",
                    "type" to "Trabajo colaborativo",
                    "purpose" to "Actividades con niños",
                    "reservation" to "Formulario online mínimo 24h antes"
                )
            ),
            
            // Sala Play Room - Biblioteca
            UniversityInfo(
                id = "sala-play-room",
                title = "Sala Play Room",
                description = "Sala especializada Play Room ubicada en el cuarto piso de la biblioteca. Espacio equipado para actividades lúdicas y entretenimiento académico.",
                category = InfoCategory.LIBRARY_SERVICES,
                location = "Biblioteca, Cuarto Piso",
                contactEmail = "biblioteca@utadeo.edu.co",
                requirements = listOf(
                    "Carnet estudiantil vigente",
                    "Reserva con 24 horas de anticipación",
                    "Mínimo 4 personas, máximo 10 personas",
                    "Uso exclusivo comunidad tadeísta"
                ),
                hours = "Lunes a Viernes: 7:00 AM - 9:00 PM, Sábados: 8:00 AM - 2:00 PM",
                availability = "Préstamo máximo 3 horas, renovable por 1 hora",
                additionalInfo = mapOf(
                    "capacity" to "4-10 personas",
                    "duration" to "Máximo 3 horas, renovable 1 hora",
                    "type" to "Trabajo colaborativo",
                    "purpose" to "Actividades lúdicas y entretenimiento",
                    "reservation" to "Formulario online mínimo 24h antes"
                )
            ),
            
            // Plaza Comic - Biblioteca
            UniversityInfo(
                id = "plaza-comic",
                title = "Plaza Comic",
                description = "Espacio Plaza Comic en el tercer piso de la biblioteca. Área abierta para trabajo colaborativo relacionado con cómics, con capacidad para múltiples grupos.",
                category = InfoCategory.LIBRARY_SERVICES,
                location = "Biblioteca, Tercer Piso",
                contactEmail = "biblioteca@utadeo.edu.co",
                requirements = listOf(
                    "Carnet estudiantil vigente",
                    "Reserva con 24 horas de anticipación",
                    "Mínimo 4 personas, máximo 10 personas",
                    "Uso exclusivo comunidad tadeísta"
                ),
                hours = "Lunes a Viernes: 7:00 AM - 9:00 PM, Sábados: 8:00 AM - 2:00 PM",
                availability = "Préstamo máximo 3 horas, renovable por 1 hora",
                additionalInfo = mapOf(
                    "capacity" to "4-10 personas",
                    "duration" to "Máximo 3 horas, renovable 1 hora",
                    "type" to "Espacio colaborativo",
                    "purpose" to "Trabajo con cómics y novelas gráficas",
                    "reservation" to "Formulario online mínimo 24h antes"
                )
            ),
            
            // Plaza Idiomas - Biblioteca
            UniversityInfo(
                id = "plaza-idiomas",
                title = "Plaza Idiomas",
                description = "Espacio Plaza Idiomas en el tercer piso de la biblioteca. Área abierta para trabajo colaborativo relacionado con idiomas, con capacidad para múltiples grupos.",
                category = InfoCategory.LIBRARY_SERVICES,
                location = "Biblioteca, Tercer Piso",
                contactEmail = "biblioteca@utadeo.edu.co",
                requirements = listOf(
                    "Carnet estudiantil vigente",
                    "Reserva con 24 horas de anticipación",
                    "Mínimo 4 personas, máximo 10 personas",
                    "Uso exclusivo comunidad tadeísta"
                ),
                hours = "Lunes a Viernes: 7:00 AM - 9:00 PM, Sábados: 8:00 AM - 2:00 PM",
                availability = "Préstamo máximo 3 horas, renovable por 1 hora",
                additionalInfo = mapOf(
                    "capacity" to "4-10 personas",
                    "duration" to "Máximo 3 horas, renovable 1 hora",
                    "type" to "Espacio colaborativo",
                    "purpose" to "Práctica de idiomas",
                    "reservation" to "Formulario online mínimo 24h antes"
                )
            ),
            
            // Plaza Exposiciones - Biblioteca
            UniversityInfo(
                id = "plaza-exposiciones",
                title = "Plaza Exposiciones",
                description = "Plaza de Exposiciones en el tercer piso de la biblioteca. Espacio utilizado para eventos culturales, muestras artísticas, talleres, conversatorios y exposiciones. Capacidad para eventos mayores.",
                category = InfoCategory.LIBRARY_SERVICES,
                location = "Biblioteca, Tercer Piso",
                contactEmail = "biblioteca@utadeo.edu.co",
                requirements = listOf(
                    "Carnet estudiantil vigente",
                    "Reserva con 24 horas de anticipación",
                    "Mínimo 4 personas, máximo 318 personas",
                    "Uso exclusivo comunidad tadeísta",
                    "Propósito del evento"
                ),
                hours = "Lunes a Viernes: 7:00 AM - 9:00 PM, Sábados: 8:00 AM - 2:00 PM",
                availability = "Préstamo máximo 3 horas, renovable por 1 hora",
                additionalInfo = mapOf(
                    "capacity" to "318 personas aproximadamente",
                    "duration" to "Máximo 3 horas, renovable 1 hora",
                    "type" to "Espacio para eventos",
                    "purpose" to "Exposiciones, eventos culturales, talleres",
                    "equipment" to "Equipamiento para eventos culturales",
                    "reservation" to "Formulario online mínimo 24h antes"
                )
            ),
            
            // Cubículos Individuales - Biblioteca
            UniversityInfo(
                id = "cubiculos-individuales",
                title = "Cubículos Individuales",
                description = "152 cubículos individuales disponibles en el cuarto piso de la biblioteca. Espacios ideales para estudio individual y trabajo concentrado.",
                category = InfoCategory.LIBRARY_SERVICES,
                location = "Biblioteca, Cuarto Piso",
                contactEmail = "biblioteca@utadeo.edu.co",
                requirements = listOf(
                    "Carnet estudiantil vigente",
                    "Uso exclusivo comunidad tadeísta"
                ),
                hours = "Lunes a Viernes: 7:00 AM - 9:00 PM, Sábados: 8:00 AM - 2:00 PM",
                availability = "Disponibles según llegada",
                additionalInfo = mapOf(
                    "capacity" to "300 personas aproximadamente en piso 4",
                    "cubicles" to "152 cubículos disponibles",
                    "purpose" to "Estudio individual",
                    "features" to "Espacios privados para concentración"
                )
            ),
            
            // Servicio de Préstamo de Salas - General Info
            UniversityInfo(
                id = "servicio-prestamo-salas",
                title = "Préstamo de Salas - Biblioteca",
                description = "Servicio de préstamo de salas de la Biblioteca UTadeo. Ofrece espacios adecuados y herramientas tecnológicas para trabajo en equipo, aprendizaje y entretenimiento con fines académicos ligados a actividades de estudio e investigación.",
                category = InfoCategory.LIBRARY_SERVICES,
                location = "Biblioteca, Pisos 3 y 4",
                contactEmail = "biblioteca@utadeo.edu.co",
                requirements = listOf(
                    "Carnet institucional personal e intransferible",
                    "Reserva con mínimo 24 horas de anticipación",
                    "Cumplir horarios establecidos",
                    "Reserva diaria por usuario",
                    "Presentar carnet al recibir la sala",
                    "Responsable del buen uso de equipos y mobiliario"
                ),
                hours = "Lunes a Viernes: 7:00 AM - 9:00 PM, Sábados: 8:00 AM - 2:00 PM",
                availability = "Sujeto a disponibilidad y reserva previa",
                additionalInfo = mapOf(
                    "max_duration" to "3 horas (renovable 1 hora más si está disponible)",
                    "capacity" to "4-10 personas por sala",
                    "waiting_time" to "Reserva respetada por 15 minutos",
                    "absent_time" to "Máximo 15 minutos ausencia, sino pierde la reserva",
                    "purpose" to "Trabajo en equipo y trabajo individual",
                    "policy" to "Una reserva diaria por usuario",
                    "verification" to "Verificar disponibilidad antes de solicitar"
                )
            ),
            
            // Préstamo de Portátiles en Kioscos
            UniversityInfo(
                id = "portatiles-kioscos",
                title = "Préstamo de Portátiles en Kioscos",
                description = "Servicio de préstamo de portátiles disponibles en kioscos. Los computadores cuentan con paquete Office instalado. Acceso inmediato según disponibilidad.",
                category = InfoCategory.EQUIPMENT_RENTAL,
                location = "Kioscos: Segundo piso Biblioteca (Módulo 21) y Cuarto piso Módulo 7 (Audiovisuales)",
                requirements = listOf(
                    "Carnet vigente de la Universidad",
                    "Estudiante matriculado",
                    "Estar al día en el pago de multas",
                    "Uso individual e intransferible"
                ),
                hours = "Lunes a Viernes: 7:00 AM - 10:00 PM, Sábados: 7:00 AM - 5:00 PM",
                availability = "3 horas máximo, renovable según disponibilidad",
                additionalInfo = mapOf(
                    "software" to "Paquete Office",
                    "time_limit" to "3 horas máximo",
                    "renewal" to "Renovable mediante renovación en el kiosco según disponibilidad",
                    "usage" to "Solo dentro de las instalaciones universitarias",
                    "return" to "Debe ser devuelto por la misma persona en el mismo sitio",
                    "penalty" to "Multa $2.000 por cada hora o fracción de demora",
                    "access" to "Acceso inmediato dependiendo de disponibilidad"
                )
            ),
            
            // Computadores con Software Especializado en Sala
            UniversityInfo(
                id = "computadores-sala-especializado",
                title = "Préstamo de Computadores con Software Especializado",
                description = "Servicio de préstamo de equipos de cómputo en sala con software especializado para carreras. Disponible mediante reserva en línea con un día de anticipación.",
                category = InfoCategory.EQUIPMENT_RENTAL,
                location = "Salas de Cómputo - diversas ubicaciones",
                requirements = listOf(
                    "Estudiante matriculado",
                    "Reserva en línea con 1 día de anticipación",
                    "Reserva en el portal de estudiantes"
                ),
                hours = "Lunes a Viernes: 7:00 AM - 10:00 PM, Sábados: 7:00 AM - 5:00 PM",
                availability = "3 horas por día",
                additionalInfo = mapOf(
                    "software" to "Software especializado para carrera",
                    "reservation" to "Reserva en línea en portal de estudiantes con 1 día de anticipación",
                    "time_limit" to "3 horas por día",
                    "reservation_steps" to "Autenticarse, seleccionar tipo de sala y grupo, elegir fecha y hora, guardar reserva",
                    "reservation_portal" to "Portal educativo - opción 'Reserva en línea de un computador en aula'",
                    "access" to "Ingresar a la sala con usuario y contraseña de correo electrónico"
                )
            ),
            
            // Salas de Uso Libre
            UniversityInfo(
                id = "salas-uso-libre",
                title = "Salas de Cómputo de Uso Libre",
                description = "Salas de cómputo disponibles para uso libre de estudiantes. No requieren reserva previa. Uso máximo 15 minutos para trabajos cortos o prioritarios.",
                category = InfoCategory.EQUIPMENT_RENTAL,
                location = "Sala 309 Módulo 7A y Sala 12 tercer piso Módulo 2",
                requirements = listOf(
                    "Estudiante matriculado",
                    "No requiere reserva para trabajos cortos"
                ),
                hours = "Lunes a Viernes: 7:00 AM - 10:00 PM, Sábados: 7:00 AM - 5:00 PM",
                availability = "Máximo 15 minutos sin reserva",
                additionalInfo = mapOf(
                    "time_limit_free" to "15 minutos máximo sin reserva",
                    "purpose" to "Trabajos cortos o prioritarios",
                    "extension" to "Para mayor tiempo, usar pantallas de autogestión para reserva en sala",
                    "salas" to "Sala 309 Módulo 7A, Sala 12 Módulo 2 (tercer piso)"
                )
            ),
            
            // Equipos Audiovisuales - Televisión
            UniversityInfo(
                id = "equipos-audiovisuales-television",
                title = "Préstamo de Equipos Audiovisuales - Televisión",
                description = "Servicio de préstamo de equipos audiovisuales del Estudio de Televisión: micrófonos (solapa Sennheiser, boom Rode, duro/mano), trípodes Manfroto y dolli, y cámaras (con acompañamiento del Auxiliar del Estudio de Televisión).",
                category = InfoCategory.EQUIPMENT_RENTAL,
                location = "Módulo 7 - Audiovisuales",
                contactEmail = "cgn.television@utadeo.edu.co",
                requirements = listOf(
                    "Solicitud por correo electrónico",
                    "Para cámaras: acompañamiento del Auxiliar del Estudio de Televisión"
                ),
                additionalInfo = mapOf(
                    "microfonos" to "Micrófonos de solapa Sennheiser, micrófonos boom Rode, micrófonos duro (de mano)",
                    "tripodes" to "Trípodes Manfroto y dolli",
                    "camaras" to "Cámaras con acompañamiento del Auxiliar del Estudio de Televisión",
                    "solicitud" to "Realizar solicitud al correo cgn.television@utadeo.edu.co"
                )
            ),
            
            // Equipos Estudio de Animación - Préstamo Interno
            UniversityInfo(
                id = "equipos-animacion-interno",
                title = "Préstamo de Equipos - Estudio de Animación (Uso Interno)",
                description = "Equipos del Estudio de Animación disponibles para préstamo interno: Computadores iMac/PC (no el Central), Mesas de Luz para Animación, Mesa Grande para Stop Motion, Truca para Fotografía Frame por Frame.",
                category = InfoCategory.EQUIPMENT_RENTAL,
                location = "Estudio de Animación - Módulo 7",
                contactEmail = "cgn.animacion@utadeo.edu.co",
                requirements = listOf(
                    "Solicitud por correo electrónico",
                    "Uso interno (dentro del salón)"
                ),
                additionalInfo = mapOf(
                    "computadores" to "Computadores iMac/PC (no el Central)",
                    "mesas_luz" to "Mesas de Luz para Animación",
                    "mesa_stopmotion" to "Mesa Grande para Stop Motion",
                    "truca" to "Truca para Fotografía Frame por Frame",
                    "uso" to "Préstamo interno (dentro del salón)",
                    "solicitud" to "Realizar solicitud al correo cgn.animacion@utadeo.edu.co"
                )
            ),
            
            // Equipos Estudio de Animación - Préstamo Externo
            UniversityInfo(
                id = "equipos-animacion-externo",
                title = "Préstamo de Equipos - Estudio de Animación (Uso Externo)",
                description = "Equipos del Estudio de Animación disponibles para préstamo fuera del salón, solo dentro de la Universidad: Cámara Profesional Nikon D5300, Luces Bajo Rango para Fotografía, Trípode, Tablas Digitalizadoras Wacom. Requiere supervisión del Auxiliar de Animación.",
                category = InfoCategory.EQUIPMENT_RENTAL,
                location = "Estudio de Animación - Módulo 7",
                contactEmail = "cgn.animacion@utadeo.edu.co",
                requirements = listOf(
                    "Solicitud por correo electrónico",
                    "Supervisión del Auxiliar de Animación",
                    "Uso solo dentro de la Universidad"
                ),
                additionalInfo = mapOf(
                    "camara" to "Cámara Profesional Nikon D5300",
                    "luces" to "Luces Bajo Rango para Fotografía",
                    "tripode" to "Trípode",
                    "tabletas" to "Tablas Digitalizadoras Wacom",
                    "supervision" to "Requiere supervisión del Auxiliar de Animación",
                    "uso" to "Préstamo fuera del salón, solo dentro de la Universidad",
                    "solicitud" to "Realizar solicitud al correo cgn.animacion@utadeo.edu.co"
                )
            ),
            
            // Reglamento General de Préstamo
            UniversityInfo(
                id = "reglamento-prestamo-equipos",
                title = "Reglamento de Préstamo de Equipos",
                description = "Disposiciones generales para el cuidado de equipos de cómputo, portátiles y equipos audiovisuales. Incluye responsabilidades del usuario, sanciones por daños y normas de uso.",
                category = InfoCategory.GENERAL_INFO,
                requirements = listOf(
                    "Aceptación de disposiciones del Reglamento Estudiantil",
                    "Carnet estudiantil vigente",
                    "Estudiante matriculado",
                    "Préstamo individual e intransferible"
                ),
                additionalInfo = mapOf(
                    "responsabilidad" to "Cada usuario es responsable del equipo durante el préstamo",
                    "informacion" to "La Universidad no se hace responsable de pérdida de información",
                    "danos" to "Aplicación de sanciones por daños según Reglamento Estudiantil Art. 58, numeral f",
                    "uso_inadecuado" to "Régimen Disciplinario Artículo 12 para uso inadecuado",
                    "reposicion" to "En caso de extravío o daño: reposición del equipo o reembolso del valor",
                    "denuncia" to "En caso de pérdida, colocar denuncia en instancias correspondientes"
                )
            ),
            
            // Computadores Biblioteca
            UniversityInfo(
                id = "computadores-biblioteca",
                title = "Computadores de Escritorio - Biblioteca",
                description = "Computadores de escritorio disponibles en la biblioteca para trabajo académico.",
                category = InfoCategory.LIBRARY_SERVICES,
                location = "Biblioteca, Piso 4",
                contactEmail = "biblioteca@utadeo.edu.co",
                hours = "Lunes a Viernes: 7:00 AM - 9:00 PM, Sábados: 8:00 AM - 2:00 PM",
                additionalInfo = mapOf(
                    "type" to "Computadores de escritorio",
                    "usage" to "Solo para uso académico",
                    "flexibility" to "No portátiles, uso en el lugar"
                )
            ),
            
            // Salas de Estudio
            UniversityInfo(
                id = "salas-estudio",
                title = "Salas de Estudio",
                description = "Salas de estudio individuales y grupales disponibles para estudiantes.",
                category = InfoCategory.CAMPUS_FACILITIES,
                location = "Múltiples ubicaciones en campus",
                hours = "Lunes a Viernes: 6:00 AM - 10:00 PM",
                availability = "Reserva anticipada recomendada",
                additionalInfo = mapOf(
                    "capacity_group" to "2-8 personas",
                    "capacity_individual" to "1 persona",
                    "reservation" to "En biblioteca o por sistema online"
                )
            ),
            
            // Biblioteca
            UniversityInfo(
                id = "biblioteca",
                title = "Biblioteca",
                description = "Biblioteca principal del campus con sala de lectura, zona de computadores y recursos académicos.",
                category = InfoCategory.LIBRARY_SERVICES,
                location = "Campus Principal",
                contactEmail = "biblioteca@utadeo.edu.co",
                hours = "Lunes a Viernes: 7:00 AM - 9:00 PM, Sábados: 8:00 AM - 2:00 PM",
                additionalInfo = mapOf(
                    "floors" to "4 pisos",
                    "services" to "Préstamo de libros, computadores, salas de estudio",
                    "resources" to "Libros, revistas académicas, bases de datos"
                )
            ),
            
            // Salas de Conferencias
            UniversityInfo(
                id = "salas-conferencias",
                title = "Salas de Conferencias",
                description = "Salas de conferencias disponibles para eventos académicos, talleres y presentaciones.",
                category = InfoCategory.ROOM_RESERVATION,
                location = "Módulo 7, Piso 3 y 4",
                contactEmail = "eventos@utadeo.edu.co",
                requirements = listOf(
                    "Reserva con anticipación",
                    "Propósito del evento",
                    "Cantidad de participantes"
                ),
                hours = "Lunes a Viernes: 8:00 AM - 7:00 PM",
                additionalInfo = mapOf(
                    "capacity" to "30-100 personas según sala",
                    "equipment" to "Proyector, sistema de sonido, pizarra"
                )
            ),
            
            // Laboratorios
            UniversityInfo(
                id = "laboratorios",
                title = "Laboratorios",
                description = "Laboratorios especializados disponibles para prácticas y proyectos académicos.",
                category = InfoCategory.CAMPUS_FACILITIES,
                location = "Módulos 3 y 5",
                contactEmail = "laboratorios@utadeo.edu.co",
                requirements = listOf(
                    "Reserva previa obligatoria",
                    "Grupo mínimo de 3 estudiantes",
                    "Supervisión de profesor"
                ),
                hours = "Lunes a Viernes: 8:00 AM - 6:00 PM",
                additionalInfo = mapOf(
                    "types" to "Computación, química, física, biología",
                    "access" to "Con reserva y aprobación del departamento"
                )
            ),
            
            // Cafetería
            UniversityInfo(
                id = "cafeteria",
                title = "Cafetería",
                description = "Cafetería del campus con servicio de comidas, snacks y bebidas.",
                category = InfoCategory.CAMPUS_FACILITIES,
                location = "Edificio Principal, Piso 1",
                hours = "Lunes a Viernes: 7:00 AM - 8:00 PM",
                additionalInfo = mapOf(
                    "payment" to "Efectivo, tarjetas, sistema de bonos",
                    "service" to "Comidas completas y snacks"
                )
            ),
            
            // Bienestar Universitario - Deportes
            UniversityInfo(
                id = "bienestar-deportes",
                title = "Bienestar Universitario - Deportes",
                description = "El área de Deportes de Bienestar Universitario ofrece servicios deportivos, recreativos y de préstamo de implementos para practicar deportes y juegos de mesa. Espacio diseñado para fomentar valores, relaciones interpersonales y convivencia en la comunidad tadeísta.",
                category = InfoCategory.CAMPUS_FACILITIES,
                location = "Módulo 24 (Carrera 4A # 23-40)",
                requirements = listOf(
                    "Carnet estudiantil, funcionario o egresado vigente",
                    "Uso exclusivo comunidad tadeísta"
                ),
                additionalInfo = mapOf(
                    "services" to "Gimnasio, cancha de squash, préstamo de implementos deportivos, juegos de mesa",
                    "purpose" to "Deporte, recreación y socialización de la comunidad universitaria"
                )
            ),
            
            // Gimnasio
            UniversityInfo(
                id = "gimnasio",
                title = "Gimnasio",
                description = "Gimnasio de 304.90 metros cuadrados en dos niveles con recepción, préstamo de implementos, batería de baños masculinos y femeninos con duchas, sanitarios, orinales, lavamanos y agua caliente, 50 lockers, sala de máquinas equipada, sala de aeróbicos y sala para entrenadores. Servicios asesorados por el Comité de Ciencias Aplicadas al Deporte en Nutrición y Fisioterapia.",
                category = InfoCategory.CAMPUS_FACILITIES,
                location = "Módulo 24 (Carrera 4A # 23-40)",
                requirements = listOf(
                    "Carnet estudiantil, funcionario o egresado vigente",
                    "Registro al ingreso"
                ),
                hours = "Lunes a Viernes: 6:00 AM - 9:00 PM, Sábados: 7:00 AM - 4:00 PM",
                additionalInfo = mapOf(
                    "area" to "304.90 m² en dos niveles",
                    "facilities" to "50 lockers, sala de máquinas, sala de aeróbicos, sala para entrenadores",
                    "advisory" to "Asesoría del Comité de Ciencias Aplicadas al Deporte (Nutrición y Fisioterapia)"
                )
            ),
            
            // Cancha de Squash
            UniversityInfo(
                id = "cancha-squash",
                title = "Cancha de Squash",
                description = "Club de Squash con 2 canchas, baterías de baños masculinos y femeninos con duchas, sanitarios, lavamanos, lockers y zona de recepción. Espacio para deporte, recreación y socialización de la comunidad universitaria.",
                category = InfoCategory.CAMPUS_FACILITIES,
                location = "Módulo 24 (Carrera 4A # 23-40)",
                contactPhone = "+57 1 5660657",
                requirements = listOf(
                    "Carnet estudiantil, funcionario o egresado vigente",
                    "Registro al ingreso",
                    "Ropa deportiva obligatoria",
                    "Zapatos de suela blanca",
                    "Gafas de protección",
                    "Implementos propios (raqueta, bola, gafas) o alquiler en recepción",
                    "Cumplimiento del reglamento del club de squash",
                    "Comportamiento adecuado (no lenguaje vulgar ni maneras sociales inadecuadas)"
                ),
                hours = "Lunes a Viernes: 6:00 AM - 9:00 PM, Sábados: 7:00 AM - 3:00 PM",
                availability = "Reserva necesaria (personalmente, por teléfono al 5660657 extensión 1265, o en línea)",
                additionalInfo = mapOf(
                    "canchas" to "2 canchas disponibles",
                    "reservation" to "Personalmente, teléfono 5660657 ext. 1265, o en línea",
                    "reservation_url" to "http://www.utadeo.edu.co/es/link/deportes/83481/squash",
                    "confirmation" to "Confirmación vía correo electrónico",
                    "equipment_rental" to "Raqueta, bola y gafas disponibles para alquiler en recepción",
                    "rules" to "No se permite ingreso de alimentos y bebidas a la zona de juego"
                )
            ),
            
            // Salón de Deportes
            UniversityInfo(
                id = "salon-deportes",
                title = "Salón de Deportes",
                description = "Espacio con servicio de cuatro mesas de tenis de mesa. Préstamo gratuito de juegos de mesa: parqués, ajedrez, dominó, jenga y uno, además del préstamo de Rana.",
                category = InfoCategory.CAMPUS_FACILITIES,
                location = "Módulo 24 (Carrera 4A # 23-40)",
                requirements = listOf(
                    "Carnet estudiantil, funcionario o egresado vigente"
                ),
                hours = "Lunes a Viernes: 9:00 AM - 8:00 PM, Sábados: 9:00 AM - 1:00 PM",
                additionalInfo = mapOf(
                    "mesas_tenis" to "4 mesas de tenis de mesa",
                    "juegos_mesa" to "Parqués, ajedrez, dominó, jenga, uno, rana",
                    "prestamo" to "Préstamo gratuito de juegos de mesa"
                )
            ),
            
            // Cancha Polifuncional
            UniversityInfo(
                id = "cancha-polifuncional",
                title = "Cancha Polifuncional",
                description = "Cancha polifuncional disponible para prácticas deportivas. Ubicada frente al módulo 7A.",
                category = InfoCategory.CAMPUS_FACILITIES,
                location = "Frente al Módulo 7A",
                requirements = listOf(
                    "Carnet estudiantil, funcionario o egresado vigente"
                ),
                additionalInfo = mapOf(
                    "type" to "Cancha para múltiples deportes",
                    "usage" to "Disponible para prácticas deportivas"
                )
            ),
            
            // Préstamo de Implementos Deportivos
            UniversityInfo(
                id = "prestamo-implementos-deportivos",
                title = "Préstamo de Implementos Deportivos",
                description = "Servicio gratuito de préstamo de balones para baloncesto, fútbol sala, fútbol, tenis y voleibol en el gimnasio. También se ofrece préstamo de raquetas para squash en el club de squash.",
                category = InfoCategory.EQUIPMENT_RENTAL,
                location = "Módulo 24 (Carrera 4A # 23-40) - Gimnasio y Club de Squash",
                requirements = listOf(
                    "Carnet vigente de estudiante, funcionario o egresado"
                ),
                additionalInfo = mapOf(
                    "balones" to "Balones para baloncesto, fútbol sala, fútbol, tenis y voleibol",
                    "ubicacion_balones" to "Préstamo en el gimnasio",
                    "raquetas" to "Raquetas para squash",
                    "ubicacion_raquetas" to "Préstamo en el club de squash",
                    "costo" to "Gratuito"
                )
            ),
            
            // Préstamo de Juegos de Mesa
            UniversityInfo(
                id = "prestamo-juegos-mesa",
                title = "Préstamo de Juegos de Mesa",
                description = "Servicio gratuito de préstamo de juegos de mesa: ajedrez, dominó, parqués y rana. Disponible en el Salón de Deportes.",
                category = InfoCategory.EQUIPMENT_RENTAL,
                location = "Módulo 24 (Carrera 4A # 23-40) - Salón de Deportes",
                requirements = listOf(
                    "Carnet vigente de estudiante, funcionario o egresado"
                ),
                hours = "Lunes a Viernes: 9:00 AM - 8:00 PM, Sábados: 9:00 AM - 1:00 PM",
                additionalInfo = mapOf(
                    "juegos" to "Ajedrez, dominó, parqués y rana",
                    "costo" to "Gratuito"
                )
            ),
            
            // Casilleros
            UniversityInfo(
                id = "casilleros-deportivos",
                title = "Casilleros Deportivos",
                description = "Servicio de casilleros gratuito para quienes hacen uso eventual del gimnasio, las canchas de squash y las canchas polifuncionales durante el tiempo que dura su práctica deportiva. También existe la posibilidad de rentar un casillero por mensualidades a muy bajo costo para usuarios frecuentes.",
                category = InfoCategory.CAMPUS_FACILITIES,
                location = "Módulo 24 (Carrera 4A # 23-40) - Gimnasio y Club de Squash",
                requirements = listOf(
                    "Uso del gimnasio, canchas de squash o canchas polifuncionales"
                ),
                additionalInfo = mapOf(
                    "uso_eventual" to "Gratuito durante la práctica deportiva",
                    "renta_mensual" to "Posibilidad de renta mensual a bajo costo para usuarios frecuentes"
                )
            )
        )
    }
    
    /**
     * Search for information relevant to a user query
     * Returns information that matches keywords in the query
     */
    fun searchRelevantInfo(query: String): List<UniversityInfo> {
        val allInfo = getAllUniversityInfo()
        val queryLower = query.lowercase()
        
        // Extract key terms from query
        val words = queryLower.split(" ").filter { it.length > 2 } // Get significant words
        
        return allInfo.filter { info ->
            val titleLower = info.title.lowercase()
            val descLower = info.description.lowercase()
            val locationLower = info.location?.lowercase() ?: ""
            
            // Direct matches - check if any word matches title, description, or location
            val wordMatch = words.any { word ->
                titleLower.contains(word) ||
                descLower.contains(word) ||
                locationLower.contains(word) ||
                info.category.name.lowercase().contains(word) ||
                info.additionalInfo.values.any { it.lowercase().contains(word) } ||
                info.requirements.any { it.lowercase().contains(word) }
            }
            
            // Full phrase match
            val phraseMatch = titleLower.contains(queryLower) ||
                    descLower.contains(queryLower) ||
                    locationLower.contains(queryLower)
            
            // Special cases for common queries
            val specialMatch = when {
                // Library-specific queries
                (queryLower.contains("plaza") || queryLower.contains("comic")) && 
                (titleLower.contains("plaza") && titleLower.contains("comic")) -> true
                
                (queryLower.contains("plaza") || queryLower.contains("idiomas")) &&
                (titleLower.contains("plaza") && titleLower.contains("idiomas")) -> true
                
                (queryLower.contains("sala") && queryLower.contains("cine")) &&
                (titleLower.contains("sala") && titleLower.contains("cine")) -> true
                
                (queryLower.contains("sala") && queryLower.contains("idiomas")) &&
                (titleLower.contains("sala") && titleLower.contains("idiomas")) -> true
                
                (queryLower.contains("sala") && queryLower.contains("comic")) &&
                (titleLower.contains("sala") && titleLower.contains("comic")) -> true
                
                (queryLower.contains("play") || queryLower.contains("room")) &&
                titleLower.contains("play room") -> true
                
                // General library room queries
                (queryLower.contains("biblioteca") || queryLower.contains("library")) &&
                (queryLower.contains("sala") || queryLower.contains("room")) &&
                info.category == InfoCategory.LIBRARY_SERVICES -> true
                
                // Available rooms queries
                (queryLower.contains("disponib") || queryLower.contains("hay")) &&
                (queryLower.contains("sala") || queryLower.contains("room")) &&
                (info.category == InfoCategory.LIBRARY_SERVICES || 
                 info.category == InfoCategory.ROOM_RESERVATION) -> true
                
                // Sports-related queries
                (queryLower.contains("deporte") || queryLower.contains("deportivo") || 
                 queryLower.contains("deportes") || queryLower.contains("gimnasio") ||
                 queryLower.contains("ejercicio") || queryLower.contains("entrenar")) &&
                (info.category == InfoCategory.CAMPUS_FACILITIES || 
                 info.category == InfoCategory.EQUIPMENT_RENTAL) &&
                (titleLower.contains("deporte") || titleLower.contains("gimnasio") ||
                 titleLower.contains("squash") || titleLower.contains("cancha") ||
                 descLower.contains("deporte") || descLower.contains("gimnasio")) -> true
                
                // Ball/ball equipment queries
                (queryLower.contains("balon") || queryLower.contains("balón") ||
                 queryLower.contains("pelota") || queryLower.contains("bola") ||
                 queryLower.contains("pelotas") || queryLower.contains("balones")) &&
                (titleLower.contains("implemento") || titleLower.contains("prestamo") ||
                 descLower.contains("balon") || descLower.contains("balón") ||
                 descLower.contains("pelota") || descLower.contains("bola")) -> true
                
                // Racket/squash equipment queries
                (queryLower.contains("raqueta") || queryLower.contains("raquetas") ||
                 queryLower.contains("squash") || (queryLower.contains("cancha") && queryLower.contains("squash"))) &&
                (titleLower.contains("squash") || titleLower.contains("raqueta") ||
                 descLower.contains("squash") || descLower.contains("raqueta")) -> true
                
                // Board games queries
                (queryLower.contains("juego") && queryLower.contains("mesa")) ||
                queryLower.contains("ajedrez") || queryLower.contains("dominó") ||
                queryLower.contains("domino") || queryLower.contains("parqué") ||
                queryLower.contains("parques") || queryLower.contains("rana") ||
                queryLower.contains("jenga") || queryLower.contains("uno") ->
                    (titleLower.contains("juego") || titleLower.contains("mesa") ||
                     descLower.contains("juego") || descLower.contains("mesa") ||
                     descLower.contains("ajedrez") || descLower.contains("dominó") ||
                     descLower.contains("parqué") || descLower.contains("rana") ||
                     descLower.contains("jenga") || descLower.contains("uno"))
                
                // Where to play sports queries
                (queryLower.contains("donde") || queryLower.contains("dónde")) &&
                (queryLower.contains("jugar") || queryLower.contains("practicar") ||
                 queryLower.contains("deporte") || queryLower.contains("futbol") ||
                 queryLower.contains("fútbol") || queryLower.contains("baloncesto") ||
                 queryLower.contains("voleibol") || queryLower.contains("tenis")) &&
                (titleLower.contains("cancha") || titleLower.contains("gimnasio") ||
                 titleLower.contains("deporte") || descLower.contains("cancha") ||
                 descLower.contains("gimnasio") || descLower.contains("deporte")) -> true
                
                // Equipment rental queries
                (queryLower.contains("prestar") || queryLower.contains("prestamo") ||
                 queryLower.contains("préstamo") || queryLower.contains("alquilar") ||
                 queryLower.contains("alquiler") || queryLower.contains("pedir")) &&
                (info.category == InfoCategory.EQUIPMENT_RENTAL ||
                 titleLower.contains("prestamo") || titleLower.contains("préstamo") ||
                 descLower.contains("prestamo") || descLower.contains("préstamo")) -> true
                
                // Gym/fitness queries
                (queryLower.contains("gimnasio") || queryLower.contains("pesas") ||
                 queryLower.contains("maquina") || queryLower.contains("máquina") ||
                 queryLower.contains("aerobico") || queryLower.contains("aeróbico")) &&
                (titleLower.contains("gimnasio") || descLower.contains("gimnasio") ||
                 descLower.contains("maquina") || descLower.contains("máquina")) -> true
                
                // Table tennis queries
                (queryLower.contains("tenis") && queryLower.contains("mesa")) ||
                queryLower.contains("ping") && queryLower.contains("pong") ->
                    (titleLower.contains("tenis") && titleLower.contains("mesa") ||
                     descLower.contains("tenis") && descLower.contains("mesa"))
                
                // Computer/equipment loan queries
                (queryLower.contains("portatil") || queryLower.contains("portátil") ||
                 queryLower.contains("laptop") || queryLower.contains("computador") ||
                 queryLower.contains("computadora") || queryLower.contains("pc")) &&
                (queryLower.contains("prestar") || queryLower.contains("prestamo") ||
                 queryLower.contains("préstamo") || queryLower.contains("alquiler") ||
                 queryLower.contains("pedir") || queryLower.contains("solicitar")) ->
                    (info.category == InfoCategory.EQUIPMENT_RENTAL ||
                     titleLower.contains("portatil") || titleLower.contains("computador") ||
                     descLower.contains("portatil") || descLower.contains("computador"))
                
                // Software specialized queries
                (queryLower.contains("software") && queryLower.contains("especializado")) ||
                (queryLower.contains("computador") && queryLower.contains("carrera")) ->
                    (titleLower.contains("especializado") || descLower.contains("especializado") ||
                     descLower.contains("carrera"))
                
                // Multimedia equipment queries (cameras, microphones, etc.)
                (queryLower.contains("camara") || queryLower.contains("cámara") ||
                 queryLower.contains("microfono") || queryLower.contains("micrófono") ||
                 queryLower.contains("microphone") || queryLower.contains("camara") ||
                 queryLower.contains("tripode") || queryLower.contains("trípode") ||
                 queryLower.contains("audiovisual") || queryLower.contains("multimedia")) &&
                (queryLower.contains("prestar") || queryLower.contains("prestamo") ||
                 queryLower.contains("préstamo") || queryLower.contains("alquiler")) ->
                    (info.category == InfoCategory.EQUIPMENT_RENTAL ||
                     titleLower.contains("audiovisual") || titleLower.contains("animacion") ||
                     descLower.contains("camara") || descLower.contains("microfono") ||
                     descLower.contains("tripode"))
                
                // Animation studio equipment queries
                (queryLower.contains("animacion") || queryLower.contains("animación") ||
                 queryLower.contains("stop") && queryLower.contains("motion") ||
                 queryLower.contains("mesa") && queryLower.contains("luz") ||
                 queryLower.contains("tabla") && queryLower.contains("digitalizadora") ||
                 queryLower.contains("wacom")) ->
                    (titleLower.contains("animacion") || titleLower.contains("animación") ||
                     descLower.contains("animacion") || descLower.contains("animación"))
                
                // Kiosk queries
                queryLower.contains("kiosco") || queryLower.contains("kioskos") ->
                    (titleLower.contains("kiosco") || descLower.contains("kiosco"))
                
                // Free use room queries
                (queryLower.contains("uso") && queryLower.contains("libre")) ||
                (queryLower.contains("libre") && queryLower.contains("uso")) ->
                    (titleLower.contains("libre") || descLower.contains("libre"))
                
                // Online reservation queries for computers
                (queryLower.contains("reserva") && queryLower.contains("linea")) ||
                (queryLower.contains("reserva") && queryLower.contains("línea")) ||
                (queryLower.contains("reserva") && queryLower.contains("online")) ->
                    (titleLower.contains("reserva") || descLower.contains("reserva") ||
                     info.category == InfoCategory.EQUIPMENT_RENTAL)
                
                else -> false
            }
            
            wordMatch || phraseMatch || specialMatch
        }
    }
    
    /**
     * Get information formatted for AI context
     */
    fun getContextForAI(query: String): String {
        val relevantInfo = searchRelevantInfo(query)
        
        if (relevantInfo.isEmpty()) {
            return "No hay información específica disponible para esta consulta."
        }
        
        val contextBuilder = StringBuilder()
        contextBuilder.append("INFORMACIÓN DISPONIBLE EN BASE DE DATOS:\n\n")
        
        relevantInfo.forEach { info ->
            contextBuilder.append("TÍTULO: ${info.title}\n")
            contextBuilder.append("DESCRIPCIÓN: ${info.description}\n")
            contextBuilder.append("UBICACIÓN: ${info.location ?: "No especificada"}\n")
            
            if (info.requirements.isNotEmpty()) {
                contextBuilder.append("REQUISITOS: ${info.requirements.joinToString(", ")}\n")
            }
            
            if (info.hours != null) {
                contextBuilder.append("HORARIOS: ${info.hours}\n")
            }
            
            if (info.contactEmail != null) {
                contextBuilder.append("EMAIL: ${info.contactEmail}\n")
            }
            
            if (info.contactPhone != null) {
                contextBuilder.append("TELÉFONO: ${info.contactPhone}\n")
            }
            
            if (info.availability != null) {
                contextBuilder.append("DISPONIBILIDAD: ${info.availability}\n")
            }
            
            if (info.additionalInfo.isNotEmpty()) {
                contextBuilder.append("INFO ADICIONAL:\n")
                info.additionalInfo.forEach { (key, value) ->
                    contextBuilder.append("- $key: $value\n")
                }
            }
            
            contextBuilder.append("\n---\n\n")
        }
        
        return contextBuilder.toString()
    }
}

