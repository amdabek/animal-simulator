package org.pon1645.ooprojekt.model;

import org.pon1645.ooprojekt.SimulationConfig;

import java.util.*;

public class GlobeMap implements IWorldMap {
    private final int width;
    private final int height;
    private final SimulationConfig config;
    private final Map<Vector2d,List<Animal>> animalsOnMap = new HashMap<>();
    private final Map<Vector2d, Grass> grasses = new HashMap<>();

    public GlobeMap(SimulationConfig cfg){
        width=cfg.width; height=cfg.height; config=cfg;
    }

    @Override
    public int getWidth(){return width;}
    @Override
    public int getHeight(){return height;}
    @Override
    public boolean canMoveTo(Vector2d position){return true;}

    public SimulationConfig getConfig(){return config;}

    public void placeAnimal(Animal animal){
        Vector2d p=animal.getPosition();
        animalsOnMap.computeIfAbsent(p,k->new ArrayList<>()).add(animal);
    }

    public void removeAnimal(Animal animal){
        Vector2d p=animal.getPosition();
        List<Animal> list=animalsOnMap.get(p);
        if(list!=null){
            list.remove(animal);
            if(list.isEmpty()){
                animalsOnMap.remove(p);
            }
        }
    }

    public boolean hasAnimalAt(Vector2d pos){
        List<Animal> list=animalsOnMap.get(pos);
        return list!=null && !list.isEmpty();
    }

    public List<Animal> getAnimalsAt(Vector2d pos){
        return animalsOnMap.get(pos);
    }

    public boolean hasGrassAt(Vector2d pos){
        return grasses.containsKey(pos);
    }

    public void removeGrassAt(Vector2d pos){
        grasses.remove(pos);
    }

    public void spawnGrassAt(Vector2d pos){
        grasses.put(pos,new Grass(pos));
    }

    public void growPlants(){
        if(config.plantGrowthVariant== PlantGrowthVariant.FOREST_CREEPING){
            growPlantsForestCreeping();
        } else {
            growPlantsEquator();
        }
    }

    private void growPlantsForestCreeping(){
        if(grasses.isEmpty()){
            spawnRandomPlants(config.plantsPerDay);
        } else {
            for(int i=0;i<config.plantsPerDay;i++){
                spawnPlantNearExisting();
            }
        }
    }

    private void spawnPlantNearExisting(){
        List<Vector2d> ex=new ArrayList<>(grasses.keySet());
        if(ex.isEmpty())return;
        Random r=new Random();
        Vector2d c=ex.get(r.nextInt(ex.size()));
        List<Vector2d> neighbors=getNeighbors(c);
        List<Vector2d> free=new ArrayList<>();
        for(Vector2d n:neighbors){
            if(!grasses.containsKey(n))free.add(n);
        }
        if(!free.isEmpty()){
            Vector2d sp=free.get(r.nextInt(free.size()));
            grasses.put(sp,new Grass(sp));
        }
    }

    private List<Vector2d> getNeighbors(Vector2d pos){
        List<Vector2d>res=new ArrayList<>();
        for(int dx=-1;dx<=1;dx++){
            for(int dy=-1;dy<=1;dy++){
                if(!(dx==0&&dy==0)){
                    int nx=pos.x+dx;int ny=pos.y+dy;
                    if(nx<0)nx=width-1;else if(nx>=width)nx=0;
                    if(ny>=0&&ny<height){
                        res.add(new Vector2d(nx,ny));
                    }
                }
            }
        }
        return res;
    }

    private void spawnRandomPlants(int howMany){
        Random r=new Random();
        int maxA=width*height;int att=0;
        while(howMany>0&&att<maxA){
            int x=r.nextInt(width);
            int y=r.nextInt(height);
            Vector2d p=new Vector2d(x,y);
            if(!grasses.containsKey(p)){
                grasses.put(p,new Grass(p));
                howMany--;
            }
            att++;
        }
    }

    private void growPlantsEquator(){
        for(int i=0;i<config.plantsPerDay;i++){
            spawnSinglePlantEquator();
        }
    }

    public void spawnSinglePlantEquator(){
        Random r=new Random();
        double c=r.nextDouble();
        int eqMin=(int)Math.floor(height*0.4);
        int eqMax=(int)Math.ceil(height*0.6);
        if(c<0.8){
            spawnPlantInEquatorRange(eqMin,eqMax);
        } else {
            spawnPlantOutsideEquatorRange(eqMin,eqMax);
        }
    }

    private void spawnPlantInEquatorRange(int eqMin,int eqMax){
        Random r=new Random();
        int attempts = width * height;
        while (attempts-- > 0) {
            int x=r.nextInt(width);
            int y=r.nextInt(eqMax-eqMin)+eqMin;
            Vector2d p=new Vector2d(x,y);
            if(!grasses.containsKey(p)){
                grasses.put(p,new Grass(p));
                return;
            }
        }
    }

    private void spawnPlantOutsideEquatorRange(int eqMin,int eqMax){
        Random r=new Random();
        int attempts = width * height;
        while (attempts-- > 0) {
            int x=r.nextInt(width);
            int y=r.nextInt(height);
            if(y<eqMin||y>=eqMax){
                Vector2d p=new Vector2d(x,y);
                if(!grasses.containsKey(p)){
                    grasses.put(p,new Grass(p));
                    return;
                }
            }
        }
    }

    public Vector2d wrapPositionIfNeeded(Vector2d oldPos,Vector2d newPos,Animal animal){
        int x=newPos.x; int y=newPos.y;
        if(x<0)x=width-1; else if(x>=width)x=0;
        if(y<0||y>=height){
            animal.reverseDirection();
            return oldPos;
        }
        return new Vector2d(x,y);
    }
    public Map<Vector2d, Grass> getGrasses() {
        return grasses;
    }

    public boolean isPreferredField(Vector2d pos) {
        switch(config.plantGrowthVariant) {
            case EQUATOR -> {
                int eqMin = (int)Math.floor(height*0.4);
                int eqMax = (int)Math.ceil(height*0.6);
                return pos.y >= eqMin && pos.y < eqMax;
            }
            case FOREST_CREEPING -> {
                List<Vector2d> neighbors = getNeighbors(pos);
                for(Vector2d n: neighbors){
                    if(grasses.containsKey(n)) return true;
                }
                return false;
            }
        }
        return false;
    }

}
