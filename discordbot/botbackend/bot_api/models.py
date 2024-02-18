from django.db import models

class Playmate(models.Model):
    id = models.AutoField(primary_key=True)
    user = models.CharField(max_length=100, blank=True)
    active = models.BooleanField(default=False) 
    wallet = models.CharField(max_length=100, blank=True)
    game_category = models.CharField(max_length=100, blank=True)
    headline_message = models.CharField(max_length=100, blank=True)

# class Advertisement(models.Model):
#     id = models.AutoField(primary_key=True)
#     game_category = models.CharField(max_length=100)
#     description = models.TextField()
#     is_active = models.BooleanField(default=True)

# class Appointment(models.Model):
#     id = models.AutoField(primary_key=True)
#     playmate = models.ForeignKey(Playmate, on_delete=models.CASCADE)
#     date_time = models.DateTimeField()
#     price = models.DecimalField(max_digits=10, decimal_places=2)

# class Appeal(models.Model):
#     id = models.AutoField(primary_key=True)
#     playmate = models.ForeignKey(Playmate, on_delete=models.CASCADE)
#     message = models.TextField()
#     ticket_number = models.CharField(max_length=50)

    # def __str__(self):
    #     return f"Challenge from {self.challenger} to {self.player} at {self.date_time}"
