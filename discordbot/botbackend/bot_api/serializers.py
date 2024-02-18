from rest_framework import serializers
from .models import Playmate

class PlaymateSerializer(serializers.ModelSerializer):
    class Meta:
        model = Playmate
        fields = '__all__'
