from django.urls import path
from . import views

urlpatterns = [
    path('get_playmate/', views.get_playmate, name='get_playmate'),
    path('set_playmate/', views.set_playmate, name='set_playmate'),

    # path('connect_wallet/', views.connect_wallet, name='connect_wallet'),
    # path('edit_wallet/', views.edit_wallet, name='edit_wallet'),
    # path('list_appointments/', views.list_appointments, name='list_appointments'),
    # path('create_appeal/', views.create_appeal, name='create_appeal'),
    # path('list_advertisements/<str:game_category>/', views.list_advertisements, name='list_advertisements'),
    # path('help_commands/', views.help_commands, name='help_commands'),
]
