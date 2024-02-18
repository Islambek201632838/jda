from celery import shared_task
from django.core.serializers import serialize
from ..bot_api.models import Flower

@shared_task
def process_filter_flowers(parameters):
    flowers = Flower.objects.all()
    # Process your data here
    return serialize('json', flowers)
