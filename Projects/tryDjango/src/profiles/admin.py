from django.contrib import admin

# Register your models here.
from .models import profile
from .models import fir

class profileAdmin(admin.ModelAdmin):
	class Meta:
		model = profile

admin.site.register(profile, profileAdmin)

class firAdmin(admin.ModelAdmin):
	class Meta:
		model = fir

admin.site.register(fir, profileAdmin)