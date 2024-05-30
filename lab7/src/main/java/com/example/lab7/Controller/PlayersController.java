package com.example.lab7.Controller;

import com.example.lab7.Entity.Players;
import com.example.lab7.Repository.PlayersRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin /* Por si se desea utilizar de IPs externas */
@RequestMapping("/players")
public class PlayersController {

    final PlayersRepo playersRepo;
    public PlayersController(PlayersRepo playersRepo) {
        this.playersRepo = playersRepo;
    }


    @GetMapping(value = { "/{region}"})
    public ResponseEntity<HashMap<String,Object>> listarPlayers(@PathVariable("region") String region) {

        HashMap<String, Object> responseJson = new HashMap<>();

        try {
            List<Players> playersByRegionSortedByMMR = playersRepo.playersSortedDescByMMR(region);
            updatePlayerPositions(playersByRegionSortedByMMR);

            List<Players> playersByRegion = playersRepo.findTop10ByRegionOrderByPositionAsc(region);

            if (!playersByRegion.isEmpty()){
                responseJson.put("result","success");
                responseJson.put("players", playersByRegion);
                return ResponseEntity.ok().body(responseJson);
            } else {
                responseJson.put("result","failure");
                responseJson.put("msg","Region no válida");
                return ResponseEntity.badRequest().body(responseJson);
            }
        } catch (Exception e) {
            responseJson.put("result","failure");
            responseJson.put("msg","Region no válida");
            return ResponseEntity.badRequest().body(responseJson);
        }
    }


    // CREAR /product y /product/
    @PostMapping(value = {"", "/"})
    public ResponseEntity<HashMap<String, Object>> guardarPlayer(@RequestBody Players player) {

        HashMap<String, Object> responseJson = new HashMap<>();

        if (player.getMmr() >= 6500) {

            if (player.getRegion().equals("Europa") || player.getRegion().equals("Américas") || player.getRegion().equals("SE Asíatico") || player.getRegion().equals("China")){
                playersRepo.save(player);
                responseJson.put("estado", "creado");
                return ResponseEntity.status(HttpStatus.CREATED).body(responseJson);
            } else {
                responseJson.put("estado", "error");
                responseJson.put("msg","Los servidores válidos son: \"Europa\", \"Américas\", \"SE Asíatico\", \"China\".");
                return ResponseEntity.badRequest().body(responseJson);
            }
        } else {
            responseJson.put("estado", "error");
            responseJson.put("msg","El mmr mínimo es 6500");
            return ResponseEntity.badRequest().body(responseJson);
        }
    }



    // ACTUALIZAR MMR
    @PutMapping(value = { "/mmr"}, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<HashMap<String, Object>> actualizarPlayer(Players playerRecibido) {

        HashMap<String, Object> rpta = new HashMap<>();

        if (playerRecibido.getPlayerId() != 0 && playerRecibido.getPlayerId() > 0) {

            Optional<Players> byId = playersRepo.findById(playerRecibido.getPlayerId());
            if (byId.isPresent()) {
                Players playerFromDb = byId.get();

                if (playerFromDb.getMmr() != null){
                    playerFromDb.setMmr(playerRecibido.getMmr());

                    if (playerRecibido.getMmr() <= 6500) {
                        playersRepo.save(playerFromDb);
                        String region = playerFromDb.getRegion();

                        List<Players> playersByRegionSortedByMMR = playersRepo.playersSortedDescByMMR(region);
                        updatePlayerPositions(playersByRegionSortedByMMR);

                        rpta.put("result", "ok");
                        return ResponseEntity.ok(rpta);

                    } else {
                        rpta.put("result", "error");
                        rpta.put("msg", "El mmr mínimo es 6500");
                        return ResponseEntity.badRequest().body(rpta);
                    }
                } else {
                    rpta.put("result", "error");
                    rpta.put("msg", "El ID del player enviado no existe");
                    return ResponseEntity.badRequest().body(rpta);
                }
            } else {
                rpta.put("result", "error");
                rpta.put("msg", "El ID del player enviado no existe");
                return ResponseEntity.badRequest().body(rpta);
            }
        } else {
            rpta.put("result", "error");
            rpta.put("msg", "debe enviar un player con ID");
            return ResponseEntity.badRequest().body(rpta);
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HashMap<String, Object>> borrarPlayer(@PathVariable("id") String idStr){

        try{
            int id = Integer.parseInt(idStr);

            HashMap<String, Object> rpta = new HashMap<>();

            Optional<Players> byId = playersRepo.findById(id);
            if(byId.isPresent()){

                Players playerToDelete = byId.get();

                String region = playerToDelete.getRegion();

                playersRepo.deleteById(id);

                List<Players> playersByRegionSortedByMMR = playersRepo.playersSortedDescByMMR(region);
                updatePlayerPositions(playersByRegionSortedByMMR);

                rpta.put("result","OK");
            }else{
                rpta.put("result","No OK");
                rpta.put("msg","el ID enviado no existe");
            }

            return ResponseEntity.ok(rpta);
        }catch (NumberFormatException e){
            return ResponseEntity.badRequest().body(null);
        }
    }



    private void updatePlayerPositions(List<Players> playersSortedByMMR) {

        for (int i = 0; i < playersSortedByMMR.size(); i++) {
            Players player = playersSortedByMMR.get(i);
            player.setPosition(i + 1);
            playersRepo.save(player);
        }
    }



}
