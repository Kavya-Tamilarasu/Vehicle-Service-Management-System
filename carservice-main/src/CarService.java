package com.example.usermanagement.service;

import com.example.usermanagement.model.Car;
import com.example.usermanagement.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    public List<Car> getAllCars() { return carRepository.findAll(); }

    public Optional<Car> getCarById(Long id) { return carRepository.findById(id); }

    public List<Car> getCarsByUserId(Long userId) { return carRepository.findByOwner_Id(userId); }

    public Car saveCar(Car car) { return carRepository.save(car); }

    public Car updateCar(Long id, Car updated) {
        Car car = carRepository.findById(id).orElseThrow();
        car.setMake(updated.getMake());
        car.setModel(updated.getModel());
        car.setYear(updated.getYear());
        car.setLicensePlate(updated.getLicensePlate());
        car.setColor(updated.getColor());
        car.setMileage(updated.getMileage());
        
        if (updated.getOwner() != null) {
            car.setOwner(updated.getOwner());
        }
        
        return carRepository.save(car);
    }

    public void deleteCar(Long id) { carRepository.deleteById(id); }
}
