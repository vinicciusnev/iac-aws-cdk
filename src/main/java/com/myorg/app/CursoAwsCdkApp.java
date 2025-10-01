package com.myorg.app;

import com.myorg.stacks.*;
import software.amazon.awscdk.App;

public class CursoAwsCdkApp {
    public static void main(final String[] args) {
        App app = new App();

        VpcStack vpcStack = new VpcStack(app, "Vpc");

        ClusterStack clusterStack = new ClusterStack(app, "Cluster", vpcStack.getVpc());
        clusterStack.addDependency(vpcStack);

        RdsStack rdsStack = new RdsStack(app, "Rds", vpcStack.getVpc());
        rdsStack.addDependency(vpcStack);

        SnsStack snsStack = new SnsStack(app, "Sns");

        ProductServiceStack productServiceStack = new ProductServiceStack(app, "ProductService", clusterStack.getCluster(), snsStack.getProductEventsTopic());
        productServiceStack.addDependency(clusterStack);
        productServiceStack.addDependency(rdsStack);
        productServiceStack.addDependency(snsStack);

        InventoryServiceStack inventoryServiceStack = new InventoryServiceStack(app, "InventoryService", clusterStack.getCluster());
        inventoryServiceStack.addDependency(clusterStack);

        app.synth();
    }
}

