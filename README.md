# 🧩 openremote-ms

Bu proje, OpenRemote platformuna entegre çalışan bir Spring Boot microservices mimarisi örneğidir. Amaç, OpenRemote üzerinde kimlik doğrulama, asset yönetimi ve merkezi yapılandırma işlemlerini mikroservis mimarisiyle gerçekleştirmektir.

---

## 📁 Proje Yapısı

```
openremote-ms/
├── auth-service/           # OpenRemote üzerinden JWT token alır
├── asset-service/          # OpenRemote Asset CRUD işlemleri yapar
├── eureka-server/          # Servis keşfi için Eureka sunucusu
├── config-server/          # Merkezi yapılandırma servisi
└── api-gateway/            # Reverse proxy ve yönlendirme katmanı
```

---

## 🧰 Kullanılan Teknolojiler

- **Java 21**
- **Spring Boot 3.x**
- **Spring Cloud (Eureka, Gateway, Config)**
- **OpenRemote**
- **Maven**
- **RestTemplate**
- **IntelliJ IDEA**
- **Lombok**, etc.

---

## 🚀 Getting Started

### Gereklilikler

- Java 21
- IntelliJ IDEA (recommended)

### Adımlar

1. **Repoyu Klonla**

```bash
git clone https://github.com/yourusername/openremote.git
cd openremote-ms
```

2. **Çalıştırma Adımları**

Not: Servislerin application.yml dosyalarında spring.cloud.config.uri, eureka.client.service-url.defaultZone gibi gerekli ayarların yapılmış olduğundan emin olun.:

```
1. config-server
2. eureka-server
3. api-gateway
4. auth-service
5. asset-service

```

---

## 🔐 Güvenlik

* Token, auth-service üzerinden alınır.

* Diğer servisler token’ı header’a Authorization: Bearer <token> şeklinde ekler.

* Token süreci dolduğunda otomatik olarak yenilenebilir şekilde cache yapılabilir.

---

## 📊 İzleme

| Tool                | URL                                |
|---------------------|-------------------------------------|
| Eureka Dashboard     | http://localhost:8761              |
| Config Server    | http://localhost:8888              |

---

## 💡 Notlar

- OpenRemote tarafında mytenant isimli bir tenant oluşturulmuş olmalı.
- myUser adında bir Service User tanımlanmalı ve gerekli roller atanmalı.
- Asset ID’leri 22 karakterlik BASE-62 UUID formatında olmalıdır.

---

## 📄 Lisans

This project is licensed under the MIT License.

---

## 🤝 Katkıda Bulunmak

İyileştirme, düzeltme veya önerileriniz için pull request gönderebilir veya issue açabilirsiniz.
