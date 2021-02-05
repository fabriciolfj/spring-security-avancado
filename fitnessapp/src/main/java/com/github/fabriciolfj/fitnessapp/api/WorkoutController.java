package com.github.fabriciolfj.fitnessapp.api;

import com.github.fabriciolfj.fitnessapp.domain.entity.Workout;
import com.github.fabriciolfj.fitnessapp.domain.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/workout")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService workoutService;

    @PostMapping
    public void add(@RequestBody final Workout workout) {
        workoutService.saveWorkout(workout);
    }

    @GetMapping
    public List<Workout> findAll() {
        return workoutService.findWorkouts();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") final Integer id) {
        workoutService.deleteWorkout(id);
    }
}
