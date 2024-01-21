plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
}

dependencies {
    implementation(project(":annotations"))
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("org.postgresql:postgresql:42.7.1")
    ksp("org.postgresql:postgresql:42.7.1")
    ksp(project(":processor"))
}

ksp {

    arg("dao.url", "jdbc:postgresql://127.0.0.1:5432/test_db")
    arg("dao.dbUsername", "test_user")
    arg("dao.dbPassword", "123")
}