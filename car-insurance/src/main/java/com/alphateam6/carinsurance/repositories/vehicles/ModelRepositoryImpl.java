package com.alphateam6.carinsurance.repositories.vehicles;

import com.alphateam6.carinsurance.exceptions.EntityNotFoundException;
import com.alphateam6.carinsurance.models.vehicles.Make;
import com.alphateam6.carinsurance.models.vehicles.VehicleModel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class ModelRepositoryImpl implements ModelRepository {

    private final SessionFactory sessionFactory;
    private final EntityManager entityManager;
    private final MakeRepository makeRepository;

    @Autowired
    public ModelRepositoryImpl(SessionFactory sessionFactory, EntityManager entityManager, MakeRepository makeRepository) {
        this.entityManager = entityManager;
        this.sessionFactory = sessionFactory;
        this.makeRepository = makeRepository;
    }

    public List<VehicleModel> getModelsByMakeId(int id){
        try (Session session = sessionFactory.openSession()) {

            Query<VehicleModel> query = session.createQuery("from VehicleModel where make.id = :make_id", VehicleModel.class);
            query.setParameter("make_id", id);

            List<VehicleModel> models = query.list();
            return models;
        }
    }

    @Override
    public List<VehicleModel> getModelsByMakeIdAndYear(int id, int year) {
        try (Session session = sessionFactory.openSession()) {

            Query<VehicleModel> query = session.createQuery("from VehicleModel where make.id = :make_id and modelYear = :modelYear", VehicleModel.class);
            query.setParameter("make_id", id);
            query.setParameter("modelYear", year);

            List<VehicleModel> models = query.list();
            return models;
        }
    }

    public List<VehicleModel> getModels(){
        try (Session session = sessionFactory.openSession()) {
            Query<VehicleModel> query = session.createQuery("from VehicleModel", VehicleModel.class);
            return query.list();
        }
    }

    public List<VehicleModel> loadData(List<VehicleModel> vehicleModels) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
//            session.createSQLQuery("truncate table vehicle_models").executeUpdate();
            for (VehicleModel vehicleModel : vehicleModels) {
                session.save(vehicleModel);
            }
            session.getTransaction().commit();
            return vehicleModels;
        }
    }

    public VehicleModel getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            VehicleModel vehicleModel = session.get(VehicleModel.class, id);
            if (vehicleModel == null) {
                throw new EntityNotFoundException(String.format("Model with id %d not found!", id));
            }
            return vehicleModel;
        }
    }

    @Override
    public VehicleModel getByModelAndYear(String model, String modelYear) {

        try (Session session = sessionFactory.openSession()) {

            Query<VehicleModel> query = session.createQuery(
                    "from VehicleModel where model = :model and modelYear = :modelYear", VehicleModel.class);
            query.setParameter("model", model);
            query.setParameter("modelYear", Integer.parseInt(modelYear));

            List<VehicleModel> models = query.list();

            if (models.isEmpty()) {
                throw new EntityNotFoundException(
                        String.format("Model %s from year %d was not found!", model, modelYear));
            }

            return models.get(0);
        }

        // todo: now -> modelYear = firstRegistration date.
        //  Maybe user should input (make, model, modelYear, firstRegDate)
    }

    public List<Integer> getModelYears(){
        try(Session session = sessionFactory.openSession()) {
            String sql = "SELECT DISTINCT model_year FROM vehicle_models ORDER BY model_year DESC";
            return (List<Integer>) session.createSQLQuery(sql).list();
        }
    }

    public List<VehicleModel> getByMakeAndYear(String make, int modelYear){
        try(Session session = sessionFactory.openSession()){
            Make vehicleMake = makeRepository.getByMake(make);
            Query<VehicleModel> query = session.createQuery(
                    "from VehicleModel WHERE make.id = :make_id and modelYear = :model_year", VehicleModel.class);
            query.setParameter("make_id", vehicleMake.getId());
            query.setParameter("model_year", modelYear);

            return query.list();
        }
    }
}
