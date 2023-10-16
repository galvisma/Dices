package com.example.dices.controllers;

import com.example.dices.models.DiceModel;
import com.example.dices.models.JsonModel;
import com.example.dices.services.DiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/v1/dices")

public class DiceController {
    @Autowired
    DiceService diceService;

    public DiceController(DiceService diceService) {
        this.diceService = diceService;
    }

    public static class ControllerConstants {
        public static final String ERROR_MESSAGE_1 = "There is no information to display";
        public static final String ERROR_MESSAGE_2 = "Dice not found with ID: ";
    }


    @GetMapping
    @ResponseBody
    public ResponseEntity<?> getDices() {
        List<DiceModel> diceList = diceService.getDices();

        if (diceList.isEmpty()) {
            String message = ControllerConstants.ERROR_MESSAGE_1;
            return ResponseEntity.status(HttpStatus.OK).body(message);

        } else {
            return ResponseEntity.ok(diceList);
        }
    }


    @PostMapping
    public ResponseEntity<?> saveDice(@RequestBody DiceModel dice) {
        ResponseEntity<?> response = diceService.createDice(dice);
        return response;
    }


    @GetMapping(path = "/{id}")
    @ResponseBody
    public ResponseEntity<?> getDiceById(@PathVariable Integer id) {

        ResponseEntity<?> response = diceService.getById(id);
        return response;
    }


    @PostMapping(path = "/{id}/rolls")
    @ResponseBody
    public ResponseEntity<?> calculateRandomNumber(@PathVariable Integer id) {
        int roll = diceService.rollDice(id);

        if (roll == 0) {
            String errorMessage = ControllerConstants.ERROR_MESSAGE_2 + id;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage); // Devuelve un mensaje de error y el código de estado 404
        } else {
            JsonModel jsonModel = new JsonModel();
            jsonModel.setId(id);
            jsonModel.setRoll(roll);
            return ResponseEntity.ok(jsonModel);
        }
    }


    @DeleteMapping(path = "/{id}")
    public String deleteDiceById(@PathVariable("id") Integer id) {
        boolean result = this.diceService.deleteDice(id);
        return null;
    }

}