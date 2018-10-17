package org.baeldung.service;

import java.util.Optional;

import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;

import org.baeldung.persistence.dao.AppointmentRepository;
import org.baeldung.persistence.dao.DoctorRepository;
import org.baeldung.persistence.dao.HospitalRepository;
import org.baeldung.persistence.dao.PatientRepository;
import org.baeldung.persistence.model.Appointment;
import org.baeldung.persistence.model.Doctor;
import org.baeldung.persistence.model.Hospital;
import org.baeldung.persistence.model.Patient;
import org.baeldung.validation.AppointmentValidater;
import org.baeldung.web.dto.AppointmentDTO;
import org.baeldung.web.dto.DoctorDTO;
import org.baeldung.web.util.AppUtil;
import org.baeldung.web.util.GenericResponse;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Transactional
public class AppointmentService extends AppointmentValidater implements IAppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    HospitalRepository hospitalRepo;
    
    @Autowired
    DoctorRepository doctorRepo;

    @Autowired
    UserService userService;

    @Autowired
    private SessionRegistry sessionRegistry;    
	@Autowired
	private SessionFactory sessionFactory;
    
    @Autowired
    private EntityManagerFactory entityManagerFactory;
	
    public static final String TOKEN_INVALID = "invalidToken";
    public static final String TOKEN_EXPIRED = "expired";
    public static final String TOKEN_VALID = "valid";

    public static String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";
    public static String APP_NAME = "SpringRegistration";


    @Override
    public GenericResponse registerNewAppointment(final AppointmentDTO appointmentDTO) throws Exception{
        Appointment appointment = new Appointment();
        appointment.setDateTime(AppUtil.todayDateTimeStr());
        appointment.setDate(AppUtil.todayDateStr());

        GenericResponse genericResponse = validate(appointmentDTO);
        if(genericResponse.getStatus().equals("FAILURE"))
        	return genericResponse;
//        if(appointment!=null && appointment.getPatient()!=null) {
//        	if(appointment.getPatient().getMobile().equals(appointmentDTO.getMobile()) && (appointment.getDateTime().split(" ")[0].equals(appointment.getDateTime().split(" ")[0])))
//           		return new AppointmentDTO();
//        }
        
       	Optional<Hospital> hospital = hospitalRepo.findById(appointmentDTO.getHospitalId());
//       	if(!hospital.isPresent())
//       		return appointmentDTO;
       	
       	Optional<Doctor> optionDoctor = doctorRepo.findById(appointmentDTO.getDoctorId());
//       	if(!optionDoctor.isPresent())
//       		return appointmentDTO;
       	
       	Doctor doctorTemp = optionDoctor.get();
       	Hospital hospitalTemp  = hospital.get();
       	
       	//Below code need to revise
       	String appointmentType = hospitalTemp.getAppointmentOfferType();
       	String appType[] = appointmentType.split("/");
       	if(appType[0].equals("count")) {
       		appointment.setPatientToVisit(Integer.valueOf(appType[1]));
       	}else {
           	int hours = Integer.valueOf(doctorTemp.getTimeOut().split(":")[0]) - Integer.valueOf(doctorTemp.getTimeIn().split(":")[0]);
           	appointment.setPatientToVisit((hours*60)/Integer.valueOf(appType[1]));
       	}
       	int toVisit = appointment.getPatientToVisit();//10;
       	Appointment appointmentTemp = appointmentRepository.getLastAppointment(hospitalTemp.getHospitalId(), doctorTemp.getDoctorId(), appointment.getDate());
       	int appointed = (appointmentTemp!=null?appointmentTemp.getPatientAppointed():0)+1;
       	int leftAppointed = toVisit - appointed;//9
       	//if appointment completed
       	if(leftAppointed<0) {
       		genericResponse.setStatus("FAILURE");
       		genericResponse.setError("Today's appointments reached limit, Please try by tomorrow");
       		return genericResponse;
       	}
       	
       	appointment.setPatientAppointed(toVisit - leftAppointed);
       	appointmentDTO.setAppntmntNo(appointment.getPatientAppointed());
       	//Register patient
       	//Get patient
       	Patient patient = patientRepository.findByPhone(appointmentDTO.getMobile());
       	if(patient==null) {
	    	patient = new Patient();
	    	patient.setName(appointmentDTO.getName());
	    	patient.setAddress(appointmentDTO.getAddress());
	    	patient.setAppointments(null);
	    	patient.setCnic(null);
	    	patient.setEmail(appointmentDTO.getEmail());
	    	patient.setMobile(appointmentDTO.getMobile());
	    	patientRepository.save(patient);
       	}
//    	SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
//    	entityManagerFactory.createEntityManager().merge(patient);
//    	Transaction tx = sessionFactory.getCurrentSession().getTransaction();
//        tx.begin();
//    	sessionFactory.getCurrentSession().saveOrUpdate(patient);
//    	tx.commit();
////    	sessionFactory.getCurrentSession().beginTransaction();
//    	entityManagerFactory.createEntityManager().joinTransaction();
    	
//    	sessionFactory.close();
//    	sessionFactory.getCurrentSession().flush();
    	
    	patientRepository.save(patient);
        
        appointment.setPatient(patient);

        //Register Hospital
        hospitalTemp.setHospitalId(appointmentDTO.getHospitalId());
        appointment.setHospital(hospitalTemp);

        //Register doctor
       	doctorTemp.setDoctorId(appointmentDTO.getDoctorId());
       	appointment.setDoctor(doctorTemp);

       	appointment.setAppointmentFee(null);
       	appointment.setAppointmentType(null);
       	
        appointmentRepository.save(appointment);
        return genericResponse;
    }


	@Override
	public void saveDoctor(DoctorDTO doctorDTO) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteDoctor(DoctorDTO doctorDTO) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Optional<DoctorDTO> fineByID(long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean isBlocked(String mobile) throws Exception {
		return (patientRepository.findByPhone(mobile) != null && patientRepository.findByPhone(mobile).isBlocked()) ?true:false;
	}


}