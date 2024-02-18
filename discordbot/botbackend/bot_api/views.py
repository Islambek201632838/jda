from django.shortcuts import get_object_or_404
from rest_framework.decorators import api_view
from rest_framework import status
from rest_framework.response import Response
from .models import *
from .serializers import *

@api_view(['GET'])
def get_playmate(request):
    user = request.query_params.get('user')
    playmate = get_object_or_404(Playmate, user=user)
    headline = playmate.headline_message
    game_category = playmate.game_category
    active = playmate.active
    
    if headline and game_category and active:
        return Response({'headline_message': playmate.headline_message,
                         'game_category': playmate.game_category,
                         'active': playmate.active})
    elif headline and game_category:
        return Response({'headline_message': playmate.headline_message,
                         'game_category': playmate.game_category})
    elif headline:
        return Response({'headline_message': playmate.headline_message})
    elif game_category:
        return Response({'game_category': playmate.game_category})
    elif not headline :
        return Response({'headline_message': 'No active headline message'})
    elif not game_category:
        return Response({'game_category': 'No active game category'})

@api_view(['PUT', 'POST'])
def set_playmate(request):
    user = request.query_params.get('user')
    message = request.data.get('headline_message', '')
    game_category = request.data.get('game_category', '')
    active = request.data.get('active', False)
    
    if not user:
        return Response({'error': 'User parameter is missing'}, status=status.HTTP_400_BAD_REQUEST)
    
    try:
        playmate, created = Playmate.objects.get_or_create(user=user)
        if message:
            playmate.headline_message = message
        if game_category:
            playmate.game_category = game_category
        if active is not None:
            playmate.active = active
        playmate.save()
        
        if created:
            return Response({'status': 'success', 'message': 'New Playmate created'})
        else:
            return Response({'status': 'success', 'message': 'Playmate updated'})
    except Exception as e:
        return Response({'error': str(e)})

@api_view(['GET'])
def find_playmate(request):
    user = request.query_params.get('user')
    gameName = request.query_params.get('gameName')
    active = request.query_params.get('active')

    if active and gameName and user:
        playmates = Playmate.objects.filter(user=user, game_category=gameName, active=True)

        serializer = PlaymateSerializer(playmates, many=True)
        return Response(serializer.data)
    
    # Return an empty list if the conditions are not met
    return Response([])
   


# @api_view(['GET'])
# def list_appointments(request):
#     user = request.query_params.get('user')
#     playmate = get_object_or_404(Playmate, user=user)
#     appointments = Appointment.objects.filter(playmate=playmate)
#     serializer = AppointmentSerializer(appointments, many=True)
#     return Response(serializer.data)

# @api_view(['GET'])
# def list_advertisements(request, game_category):
#     advertisements = Advertisement.objects.filter(game_category=game_category, is_active=True)
#     serializer = AdvertisementSerializer(advertisements, many=True)
#     return Response(serializer.data)

# @api_view(['POST'])
# def create_appeal(request):
#     user = request.query_params.get('user')
#     message = request.data.get('message', '')
#     ticket_number = request.data.get('ticket_number', '')

#     if not message or not ticket_number:
#         return Response({'error': 'Missing parameters'}, status=400)

#     playmate = get_object_or_404(Playmate, user=user)

#     # Create the appeal
#     appeal = Appeal.objects.create(
#         playmate=playmate,
#         message=message,
#         ticket_number=ticket_number
#     )

#     return Response({'message': 'Appeal created successfully'}, status=201)
