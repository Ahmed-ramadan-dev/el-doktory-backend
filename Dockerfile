# المرحلة الأولى: بناء المشروع باستخدام Maven و Java 17
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app

# نسخ ملفات الإعدادات أولاً لتحسين سرعة البناء (Caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# نسخ الكود وبناء ملف الـ JAR
COPY src ./src
RUN mvn clean package -DskipTests

# المرحلة الثانية: تشغيل المشروع بنسخة خفيفة
FROM openjdk:17-jdk-slim
WORKDIR /app

# نسخ ملف الـ jar الناتج من المرحلة الأولى
# ملاحظة: اسم الملف في pom.xml هو El-Doktory-0.0.1-SNAPSHOT.jar
COPY --from=build /app/target/El-Doktory-0.0.1-SNAPSHOT.jar app.jar

# فتح المنفذ (البورت)
EXPOSE 8080

# أمر التشغيل النهائي
ENTRYPOINT ["java", "-jar", "app.jar"]
